import json
import logging
from time import sleep

from kafka import KafkaProducer
from itemadapter import ItemAdapter

from news_bot.models.items import NewsCrawlerKafkaItem

logger = logging.getLogger(__name__)


class KafkaPipeline:

    def __init__(self, kafka_host, kafka_port, kafka_topic):
        self.kafka_host = kafka_host
        self.kafka_port = kafka_port
        self.kafka_topic = kafka_topic

        self.producer = KafkaProducer(bootstrap_servers=f'{self.kafka_host}:{self.kafka_port}',
                                      value_serializer=lambda v: json.dumps(v).encode('utf-8'))

    @classmethod
    def from_crawler(cls, crawler):
        try:
            return cls(
                kafka_host=crawler.settings.get('KAFKA_HOST'),
                kafka_port=crawler.settings.get('KAFKA_PORT'),
                kafka_topic=crawler.settings.get('ARTICLE_TOPIC')
            )
        except Exception as e:
            logger.error(str(e))

    def open_spider(self, spider):
        pass

    def close_spider(self, spider):
        self.producer.flush()
        self.producer.close()

    def process_item(self, item, spider):
        try:
            inserted_item = NewsCrawlerKafkaItem()
            inserted_item['raw_url'] = item['raw_url']
            inserted_item['domain'] = item['domain']
            inserted_item['title'] = item['title']
            inserted_item['content'] = item['content']
            inserted_item['author_display_name'] = item['author_display_name']
            inserted_item['first_crawled_time'] = item['first_crawled_time']
            inserted_item['published_time'] = item['published_time']
            inserted_item['summary'] = item['summary']

            list_image_sources = item['image_sources']
            while "" in list_image_sources:
                list_image_sources.remove("")
            inserted_item['image_sources'] = list_image_sources

            list_video_sources = item['video_sources']
            while "" in list_video_sources:
                list_video_sources.remove("")
            inserted_item['video_sources'] = list_video_sources

            list_tags = item['tag']
            list_tags_normalize = []
            while "" in list_tags:
                list_tags.remove("")
            for tag in list_tags:
                list_tags_normalize.append(tag.strip())
            inserted_item['tag'] = list_tags_normalize

            list_share_content = item['share_content']
            while "" in list_share_content:
                list_share_content.remove("")
            inserted_item['share_content'] = list_share_content

            logger.info(f"KafkaPipeline-processItem done {inserted_item['raw_url']}")

            self.producer.send(self.kafka_topic, ItemAdapter(inserted_item).asdict())
        except Exception as e:
            logger.error("KafkaPipeline-processItem failed: " + str(e))

        return item
