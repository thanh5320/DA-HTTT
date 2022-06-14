import logging
import urllib
from datetime import datetime
from urllib.request import urlopen

import scrapy
from ftfy import fix_text
from scrapy.http import HtmlResponse
from w3lib.html import remove_tags, replace_entities, remove_comments

from news_bot.models.items import NewsCrawlerItem
from news_bot.utils.utils import HelperFunction


class XpathSpider(scrapy.Spider):
    name = '{}_bot'
    allowed_domains = []
    list_sub_domains = []
    xpaths = {}
    date = ''
    domain = ''
    base_url = ''
    custom_settings = {
        'RANDOMIZE_DOWNLOAD_DELAY': True,
        'LOG_LEVEL': 'DEBUG',
        'ITEM_PIPELINES': {
            'news_bot.pipelines.data_crawler.NewsCrawlerPipeline': 250,
            'news_bot.pipelines.data_normalize.DataNormalizePipeline': 300,
        }
    }

    # headers = {"user-agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"}
    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'Accept-Encoding': 'gzip, deflate, br',
        'Accept-Language': 'en-US,en;q=0.8',
        'Connection': 'keep-alive',
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36'
    }

    def __init__(self, *args, **kwargs):
        logger = logging.getLogger('scrapy.middleware')
        logger.setLevel(logging.WARNING)

        super().__init__(*args, **kwargs)

        self.date = datetime.today().strftime("%Y-%m-%d")
        domainDict = kwargs.get('domainDict')
        if domainDict is None:
            print('domainDict is empty')
            return

        self.domain = HelperFunction.normalize_url(domainDict.get('domain'))
        self.name = self.name.format(self.domain)
        self.base_url = domainDict.get('domain')
        self.allowed_domains.append(self.domain)
        self.xpaths.update({
            'all_links': domainDict.get('all_links'),
            'all_subs': domainDict.get('all_subs'),
            'next_page': domainDict.get('next_page'),
            'author_display_name': domainDict.get('author_display_name'),
            'published_time': domainDict.get('published_time') if domainDict.get('published_time') else '',
            'content': domainDict.get('content'),
            'image_sources': domainDict.get('image_sources'),
            'video_sources': domainDict.get('video_sources'),
            'tags': domainDict.get('tags'),
            'share_content': domainDict.get('share_content'),
            'raw_html': domainDict.get('raw_html'),
        })

    def start_requests(self):
        yield scrapy.Request(url=self.base_url, callback=self.parse)

    def parse(self, response, **kwargs):
        self.logger.info(
            f'[BOT_CRAWLER_NUM_MSG_IN_QUEUE] parse {response.url} = {len(self.crawler.engine.slot.scheduler)}')
        all_news_links = response.xpath(self.xpaths.get('all_links')).getall()
        self.logger.info(f'{response.url} has {len(all_news_links)} articles found.')
        articles = []
        for link in all_news_links:
            if link.startswith('/'):
                link = self.base_url + link
            elif not link.startswith('https') and not link.startswith('http'):
                link = self.base_url + '/' + link
            articles.append(link)

        for item in articles:
            yield scrapy.Request(url=item, headers=self.headers, callback=self.parse_detail)

        # use all_subs selector to extract all sub-paths
        subdomains = response.xpath(self.xpaths.get('all_subs')).getall()
        self.logger.info(f'{self.domain} has {len(subdomains)} sub-paths.')
        subqueue = []
        for subdomain in subdomains:
            if subdomain.find("javascript") != -1 or subdomain.strip() == '':
                continue
            if subdomain.find('%20') != -1:
                subdomain = subdomain.replace("%20", "")
            if subdomain.startswith('/'):
                subdomain = f'{self.base_url}{subdomain}'
            if subdomain.startswith('/'):
                subdomain = response.url + subdomain
            elif not subdomain.startswith('https') and not subdomain.startswith('http') or subdomain == 'dan-toc-mien-nui':
                subdomain = f'{self.base_url}/{subdomain}'
            subqueue.append(subdomain)

        for sub in subqueue:
            yield scrapy.Request(url=sub, callback=self.parse_sub_domain)

    def parse_sub_domain(self, response):
        self.logger.info(
            f'[BOT_CRAWLER_NUM_MSG_IN_QUEUE] parse_sub_domain {response.url} = {len(self.crawler.engine.slot.scheduler)}')
        all_news_links = response.xpath(self.xpaths.get('all_links')).getall()
        self.logger.info(f'{response.url} has {len(all_news_links)} articles found.')
        articles = []
        for link in all_news_links:
            if link.startswith('/'):
                link = self.base_url + link
            articles.append(link)

        for item in articles:
            yield scrapy.Request(url=item, headers=self.headers, callback=self.parse_detail)

    def parse_detail(self, response):
        self.logger.info(
            f'[BOT_CRAWLER_NUM_MSG_IN_QUEUE] parse_detail {response.url} = {len(self.crawler.engine.slot.scheduler)}')

        if response.text and not response.text[:10].isascii():
            # response.text is not String
            req = urllib.request.Request(response.url, headers={'User-Agent': 'Mozilla/5.0'})
            con = urllib.request.urlopen(req)
            new_response = con.read()
            new_response_text = fix_text(str(new_response, 'utf-8'))
            response = HtmlResponse(url=response.url, encoding='utf-8', body=new_response_text)

        title, description, published_time, tag_str = HelperFunction.parse_meta_from_tags(response.text)

        if published_time == '':
            time = response.xpath("//meta[@property='article:published_time']/@content").get()
            published_time = HelperFunction.normalize_published_date(time)
            try:
                datetime.strptime(published_time, "%d/%m/%Y %H:%M:%S").strftime("%Y/%m/%d %H:%M:%S")
            except:
                published_time = None

        if published_time is None or (
                published_time == '' and title != '' and self.xpaths.get('published_time') is not None):
            parsed = response.xpath(self.xpaths.get('published_time')).getall()
            if parsed:
                if len(parsed) == 1:
                    published_time = HelperFunction.normalize_published_date(parsed[0].strip())
                elif len(parsed) > 1:
                    time = " ".join(parsed)
                    published_time = HelperFunction.normalize_published_date(time.strip())

        if published_time and published_time != "":
            today = datetime.today()
            date_str = today.strftime("%Y/%m/%d %H:%M:%S")

            author_parsed = ''
            if self.xpaths.get('author_display_name') and self.xpaths.get('author_display_name') != '':
                author_parsed = response.xpath(self.xpaths.get('author_display_name')).get()

            if "node()" in self.xpaths.get('content'):
                content = "".join(response.xpath(self.xpaths.get('content').extract()))
            else:
                content = "".join(response.xpath(self.xpaths.get('content')).getall())

            content = replace_entities(content)
            content = remove_comments(content)
            content = remove_tags(content)

            share_content = []
            if self.xpaths.get('share_content') and self.xpaths.get('share_content') != '':
                share_content = response.xpath(self.xpaths.get('share_content')).getall()

            image_sources = []
            if self.xpaths.get('image_sources') and self.xpaths.get('image_sources') != '':
                images_parsed = response.xpath(self.xpaths.get('image_sources')).getall()
                if images_parsed and len(images_parsed) > 0:
                    image_sources = list(set(images_parsed))

            video_sources = []
            if self.xpaths.get('video_sources') and self.xpaths.get('video_sources') != '':
                videos_parsed = response.xpath(self.xpaths.get('video_sources')).getall()
                if videos_parsed and len(videos_parsed) > 0:
                    video_sources = list(set(videos_parsed))

            if tag_str == '':
                if self.xpaths.get('tags') and self.xpaths.get('tags') != '':
                    tags = response.xpath(self.xpaths.get('tags')).getall()
                    tag_str = ','.join(tags)

            raw_html = ''
            if self.xpaths.get('raw_html') and self.xpaths.get('raw_html') != '':
                raw_html = response.xpath(self.xpaths.get('raw_html')).getall()

            return NewsCrawlerItem(
                raw_url=response.url,
                domain=self.base_url,
                title=title,
                content=content,
                summary=description,
                image_sources=image_sources,
                video_sources=video_sources,
                share_content=share_content,
                published_time=published_time,
                first_crawled_time=date_str,
                author_display_name=author_parsed,
                tag=tag_str,
                raw_html=raw_html
            )
