import logging
import sys
import urllib
from datetime import datetime
from multiprocessing.process import current_process
from time import sleep
from urllib.parse import urlparse
from urllib.request import urlopen

import scrapy
from ftfy import fix_text
from scrapy.http import HtmlResponse
from w3lib.html import remove_tags, replace_entities, remove_comments

from news_bot.models.items import NewsCrawlerItem
from news_bot.utils.utils import HelperFunction

logger_main = logging.getLogger(__name__)


class NewsSpider(scrapy.Spider):
    name = 'news_bot'

    def __init__(self, list_data=None, *args, **kwargs):
        self.pid = current_process().pid
        logger_main.info('[NEWS_CRAWLER] Process ' + str(self.pid) + ' is starting')

        logger_core_scraper = logging.getLogger('scrapy.core.scraper')
        logger_core_scraper.setLevel(logging.ERROR)

        super(NewsSpider, self).__init__(*args, **kwargs)

        if not isinstance(list_data, list) or len(list_data) == 0:
            logger_main.error('list data not found/empty')
            sys.exit(-1)

        self.list_domains = list_data
        logger_main.info(f'[NEWS_CRAWLER] Process {str(self.pid)} is starting, with {len(list_data)} domains')

    def start_requests(self):
        for domain_dict in self.list_domains:
            url_to_crawl = domain_dict[13] + "://" + domain_dict[1]
            logger_main.info(f'[NEWS_CRAWLER] Process {str(self.pid)} - domain : {url_to_crawl}')
            yield scrapy.Request(url=url_to_crawl, callback=self.parse, meta={"xpaths": domain_dict})

    def parse(self, response, **kwargs):
        xpaths = response.meta['xpaths']
        ''' xPath infos:
            [1]:    domain
            [2]:    all_links
            [3]:    all_subs
            [4]:    next_page
            [5]:    content
            [6]:    image_sources
            [7]:    video_sources
            [8]:    author_display_name
            [9]:    tags
            [10]:   published_time
            [11]:   share_content
            [12]:   raw_html
            [13]:   scheme
        '''

        full_domain_url = f'{xpaths[13]}://{xpaths[1]}'
        all_news_links = response.xpath(xpaths[2].replace("'*'", "*")).getall()
        logger_main.info(f'Process {str(self.pid)} : {response.url} has {len(all_news_links)} articles found.')
        articles = []
        for link in all_news_links:
            if link.find('javascript') != -1:
                continue
            if link.startswith('/'):
                link = f'{full_domain_url}{link}'
            elif not link.startswith('https') and not link.startswith('http'):
                link = f'{full_domain_url}/{link}'
            articles.append(link)

        for item in articles:
            yield scrapy.Request(url=item, callback=self.parse_detail, meta={'xpaths': xpaths})

        # use all_subs selector to extract all sub-paths
        subdomains = response.xpath(xpaths[3].replace("'*'", "*")).getall()
        logger_main.info(f'Process {str(self.pid)} : {response.url} has {len(subdomains)} sub-paths.')
        sub_queue = []
        for subdomain in subdomains:
            if subdomain.find("javascript") != -1 or subdomain.strip() == '':
                continue
            if subdomain.find('%20') != -1:
                subdomain = subdomain.replace("%20", "")
            if subdomain.startswith('/'):
                subdomain = f'{full_domain_url}{subdomain}'
            elif not subdomain.startswith('https') and not subdomain.startswith(
                    'http') or subdomain == 'dan-toc-mien-nui':
                subdomain = f'{full_domain_url}/{subdomain}'
            sub_queue.append(subdomain)

        for sub in sub_queue:
            yield scrapy.Request(url=sub, callback=self.parse_sub_domain, meta={'xpaths': xpaths})

    def parse_sub_domain(self, response):
        xpaths = response.meta['xpaths']

        all_news_links = response.xpath(xpaths[2].replace("'*'", "*")).getall()
        full_domain_url = f'{xpaths[13]}://{xpaths[1]}'

        logger_main.info(f'Process {str(self.pid)} : {response.url} has {len(all_news_links)} articles found.')
        articles = []
        for link in all_news_links:
            if link.startswith('/'):
                link = f'{full_domain_url}{link}'
            articles.append(link)

        for item in articles:
            yield scrapy.Request(url=item, callback=self.parse_detail, meta={'xpaths': xpaths})

    def parse_detail(self, response):
        sleep(.5)

        xpaths = response.meta['xpaths']

        if response.text and not response.text[:10].isascii():
            # response.text is not String
            logger_main.info(
                f'[NEWS_CRAWLER_ZIPPED_RESPONSE] parse_detail {response.url} with zipped response of {response.text[:5]} ...')
            req = urllib.request.Request(response.url, headers={'User-Agent': 'Mozilla/5.0'})
            con = urllib.request.urlopen(req)
            new_response = con.read()
            new_response_text = fix_text(str(new_response, 'utf-8'))
            response = HtmlResponse(url=response.url, encoding='utf-8', body=new_response_text)

        try:
            title, description, published_time, tag_str = HelperFunction.parse_meta_from_tags(response.text)

            if title == '':
                title = response.xpath("//title/text()").get()
            if description == '':
                description = response.xpath("//meta[@name='description']/@content").get()

            if published_time == '':
                time = response.xpath(
                    "//meta[@name='article:published_time' or @property='article:published_time']/@content").get()
                published_time = HelperFunction.normalize_published_date(time)
                try:
                    datetime.strptime(published_time, "%d/%m/%Y %H:%M:%S").strftime("%Y/%m/%d %H:%M:%S")
                except:
                    try:
                        datetime.strptime(published_time, "%Y/%m/%d %H:%M:%S").strftime("%Y/%m/%d %H:%M:%S")
                    except:
                        published_time = ''

            if published_time == '' and xpaths[10] is not None and xpaths[10] != '':
                try:
                    parsed = response.xpath(xpaths[10].replace("'*'", "*")).getall()
                    if parsed:
                        if len(parsed) == 1:
                            published_time = HelperFunction.normalize_published_date(parsed[0].strip())
                        elif len(parsed) > 1:
                            time = " ".join(parsed)
                            published_time = HelperFunction.normalize_published_date(time.strip())
                except Exception as e:
                    logger_main.info("[NEWS_CRAWLER_ERR] Parse error : " + str(e))
                    published_time = ''

            try:
                datetime.strptime(published_time + " 00:00:00", "%Y/%m/%d %H:%M:%S").strftime("%Y/%m/%d %H:%M:%S")
                published_time = published_time + " 00:00:00"
            except Exception as e:
                print("Parse : " + str(e))

            date = datetime.today().strftime("%Y-%m-%d")

            if published_time == "":
                logger_main.info(f'[NEWS_CRAWLER] [published_time is empty] {response.url}')
            else:
                if HelperFunction.check_time_crawl(date, published_time):
                    date_str = datetime.today().strftime("%Y/%m/%d %H:%M:%S")

                    author_parsed = ''
                    if xpaths[8] and xpaths[8] != '':
                        author_parsed = response.xpath(xpaths[8].replace("'*'", "*")).get()

                    content = "".join(response.xpath(xpaths[5].replace("'*'", "*")).getall())
                    content = replace_entities(content)
                    content = remove_comments(content)
                    content = remove_tags(content)

                    share_content = []
                    if xpaths[11] and xpaths[11] != '':
                        share_content = response.xpath(xpaths[11].replace("'*'", "*")).getall()

                    image_sources = []
                    if xpaths[6] and xpaths[6] != '':
                        images_parsed = response.xpath(xpaths[6].replace("'*'", "*")).getall()
                        if images_parsed and len(images_parsed) > 0:
                            image_sources = list(set(images_parsed))

                    video_sources = []
                    if xpaths[7] and xpaths[7] != '':
                        videos_parsed = response.xpath(xpaths[7].replace("'*'", "*")).getall()
                        if videos_parsed and len(videos_parsed) > 0:
                            video_sources = list(set(videos_parsed))

                    if tag_str == '':
                        if xpaths[9] and xpaths[9] != '':
                            tags = response.xpath(xpaths[9].replace("'*'", "*")).getall()
                            tag_str = ','.join(tags)

                    raw_html = ''
                    if xpaths[12] and xpaths[12] != '':
                        raw_html = response.xpath(xpaths[12].replace("'*'", "*")).getall()

                    logger_main.info(f'[NEWS_CRAWLER_DONE] parse_detail {response.url}')

                    domain = urlparse(response.url).hostname

                    return NewsCrawlerItem(
                        raw_url=response.url,
                        domain=domain,
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
                else:
                    logger_main.info(f'[NEWS_CRAWLER] [published_time is not today] parse_detail {response.url}')
        except Exception as err:
            logger_main.error(f'[NEWS_CRAWLER_ERR] parse_detail {response.url} failed: {str(err)}')
