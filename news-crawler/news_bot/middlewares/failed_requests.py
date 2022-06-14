from random_user_agent.params import SoftwareName, OperatingSystem
from random_user_agent.user_agent import UserAgent
from scrapy import signals


class FailedRequestsMiddleware(object):

    def __init__(self, failed_list, stats):
        self.failed_list = failed_list
        self.ua_rotator = None
        self.stats = stats

    @classmethod
    def from_crawler(cls, crawler):
        list_failed_reqs = crawler.spider.failed_requests
        o = cls(list_failed_reqs, crawler.stats)
        crawler.signals.connect(o.spider_opened, signal=signals.spider_opened)
        crawler.signals.connect(o.spider_closed, signal=signals.spider_closed)
        return o

    def spider_opened(self):
        software_names = [SoftwareName.CHROME.value]
        operating_systems = [OperatingSystem.WINDOWS.value, OperatingSystem.LINUX.value]
        self.ua_rotator = UserAgent(software_names=software_names, operating_systems=operating_systems, limit=100)

    def spider_closed(self):
        self.failed_list = []

    def process_request(self, request, spider):
        # must return an iterable of Request objects and item object.
        if 'change-ua' in request.meta and request.meta['change-ua'] == True:
            request.headers['user-agent'] = self.ua_rotator.get_random_user_agent()
            spider.logger.info(f'[NEWS_CRAWLER] UA overwritten : {request.url}')

