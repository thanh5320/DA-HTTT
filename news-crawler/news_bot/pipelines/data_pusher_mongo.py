import logging
from asyncio import sleep

from itemadapter import ItemAdapter
from pymongo import MongoClient

from news_bot.models.items import NewsCrawlerMongoItem

logger = logging.getLogger(__name__)


class MongoPipeline:

    def __init__(self, mongo_host, mongo_port, mongo_user, mongo_password, mongo_auth_db, mongo_collection):
        self.mongo_host = mongo_host
        self.mongo_port = mongo_port
        self.mongo_user = mongo_user
        self.mongo_password = mongo_password
        self.mongo_collection = mongo_collection
        self.client = MongoClient(
            host=mongo_host,
            port=mongo_port,
            username=mongo_user,
            password=mongo_password)
        self.mongo_auth_db = self.client[mongo_auth_db]

    @classmethod
    def from_crawler(cls, crawler):
        try:
            return cls(
                mongo_host=crawler.settings.get('MONGO_HOST'),
                mongo_port=crawler.settings.get('MONGO_PORT'),
                mongo_user=crawler.settings.get('MONGO_USER'),
                mongo_password=crawler.settings.get('MONGO_PWD'),
                mongo_auth_db=crawler.settings.get('MONGO_AUTH_DB'),
                mongo_collection=crawler.settings.get('MONGO_COLLECTION')
            )
        except Exception as e:
            logger.error(str(e))

    def open_spider(self, spider):
        pass

    def close_spider(self, spider):
        self.client.close()

    def process_item(self, item, spider):
        inserted_item = NewsCrawlerMongoItem()
        inserted_item['raw_url'] = item['raw_url']
        inserted_item['raw_html'] = item['raw_html']
        self.mongo_auth_db[self.mongo_collection].insert_one(ItemAdapter(inserted_item).asdict())
        return item
