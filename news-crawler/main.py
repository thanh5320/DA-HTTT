import os
from datetime import datetime
from multiprocessing import Process

from scrapy.crawler import CrawlerProcess
from scrapy.utils.log import configure_logging
from scrapy.utils.project import get_project_settings
from twisted.internet import asyncioreactor

from news_bot.models.postgresql import Postgresql
from news_bot.spiders.news_spider import NewsSpider
from news_bot.utils.utils import HelperFunction

asyncioreactor.install()


def crawl_news(domains):
    runner = CrawlerProcess(get_project_settings())
    runner.crawl(NewsSpider, list_data=domains)
    runner.start()


def main():
    max_process_count = os.cpu_count()
    max_domains_per_process = 100
    limit = max_process_count * max_domains_per_process
    offset = 0

    while True:
        print(f'[MAIN_CRAWLER] Current time {datetime.today().strftime("%Y/%m/%d %H:%M:%S")} ...')
        list_data = Postgresql.getInstance().get_crawlable_domains(offset, limit)
        print(f'[MAIN_CRAWLER] Prepare list of {len(list_data)} domains')

        processes = []
        domain_groups = HelperFunction.split_list(list_data, round(len(list_data) / max_process_count))
        for group in domain_groups:
            crawl_news(group)
            # p = Process(target=crawl_news, args=(group,))
        #     processes.append(p)
        #     p.start()
        # for proc in processes:
        #     proc.join()

        print(
            f'[MAIN_CRAWLER] Done crawling {limit} domains from offset {offset}, preparing next group from offset {offset + limit}')
        offset += limit

        if offset >= len(list_data):
            offset = 0


if __name__ == "__main__":
    configure_logging()
    main()
