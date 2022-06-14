import json
from datetime import datetime
from multiprocessing import Process

from scrapy.crawler import CrawlerProcess
from scrapy.utils.project import get_project_settings
from twisted.internet import reactor

from news_bot.models.postgresql import Postgresql
from news_bot.spiders.xpath_test_spider import XpathSpider
from news_bot.utils.utils import HelperFunction


def fork(domainDict):
    s = get_project_settings()
    s.update({
        "FEED_EXPORTERS": {
            'csv': 'news_bot.utils.exporters.CsvCustomSeparator'
        },
        "ITEM_PIPELINES": {
            'news_bot.pipelines.data_crawler.NewsCrawlerPipeline': 250,
            'news_bot.pipelines.data_normalize.DataNormalizePipeline': 300
        }
    })

    runner = CrawlerProcess(s)
    deferred = runner.crawl(XpathSpider, domainDict=domainDict)
    deferred.addBoth(lambda _: reactor.stop())
    reactor.run()


def crawl_news(data):
    domains = data.get('domains')
    processes = []

    for domainDict in domains:
        domain = domainDict.get('domain').replace(".", "_")
        domain_name = HelperFunction.normalize_url(domain)

        p = Process(name=f"crawl-news-{domain_name}", target=fork, args=(domainDict,))
        processes.append(p)
        p.start()

    for process in processes:
        process.join()


if __name__ == "__main__":
    with open('config/top_traffic_domains_2.json', encoding='utf=8') as f:
        data_json = json.load(f)

    # Postgresql.getInstance().check_postgres_data(data_json)
    crawl_news(data_json)

