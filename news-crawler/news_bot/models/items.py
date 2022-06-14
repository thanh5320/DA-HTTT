import scrapy


class NewsCrawlerItem(scrapy.Item):
    raw_url = scrapy.Field()
    domain = scrapy.Field()
    title = scrapy.Field()
    tag = scrapy.Field()
    content = scrapy.Field()
    author_display_name = scrapy.Field()
    published_time = scrapy.Field()
    first_crawled_time = scrapy.Field()
    share_content = scrapy.Field()
    video_sources = scrapy.Field()
    image_sources = scrapy.Field()
    summary = scrapy.Field()
    raw_html = scrapy.Field()


class NewsCrawlerKafkaItem(scrapy.Item):
    raw_url = scrapy.Field()
    domain = scrapy.Field()
    title = scrapy.Field()
    tag = scrapy.Field()
    content = scrapy.Field()
    author_display_name = scrapy.Field()
    published_time = scrapy.Field()
    first_crawled_time = scrapy.Field()
    share_content = scrapy.Field()
    video_sources = scrapy.Field()
    image_sources = scrapy.Field()
    summary = scrapy.Field()


class NewsCrawlerMongoItem(scrapy.Item):
    _id = scrapy.Field()
    raw_url = scrapy.Field()
    raw_html = scrapy.Field()
