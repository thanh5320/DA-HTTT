BOT_NAME = 'xsocial_news_bot'

SPIDER_MODULES = ['news_bot.spiders']
NEWSPIDER_MODULE = 'news_bot.spiders'

FEED_EXPORT_ENCODING = 'utf-8'
FEED_EXPORTERS = {
    'csv': 'news_bot.utils.exporters.CsvCustomSeparator'
}

DEFAULT_REQUEST_HEADERS = {
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'en',
}

RANDOMIZE_DOWNLOAD_DELAY = True

DUPEFILTER_DEBUG = False
DUPEFILTER_CLASS = 'scrapy.dupefilters.BaseDupeFilter'

# Crawl responsibly by identifying yourself (and your website) on the user-agent
USER_AGENT = 'Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36'

ROBOTSTXT_OBEY = False

# See also autothrottle settings and docs
DOWNLOAD_DELAY = 2

CONCURRENT_REQUESTS = 5000
CONCURRENT_ITEMS = 1000
CONCURRENT_REQUESTS_PER_DOMAIN = 5000

# Disable cookies (enabled by default)
COOKIES_ENABLED = False

TELNETCONSOLE_PORT = None

ITEM_PIPELINES = {
    'news_bot.pipelines.data_crawler.NewsCrawlerPipeline': 250,
    'news_bot.pipelines.data_normalize.DataNormalizePipeline': 300,
    'news_bot.pipelines.data_pusher_kafka.KafkaPipeline': 301,
    #'news_bot.pipelines.data_pusher_mongo.MongoPipeline': 302,
}

WEBSERVICE_ENABLED = False

RANDOM_UA_ENABLED = True
RANDOM_UA_DEFAULT_TYPE = 'mobile'
# always change user-agent
RANDOM_UA_OVERWRITE = True

RETRY_ENABLED = False

# MONGO
# MONGO_HOST = 'mh-x1'
# MONGO_PORT = 27017
# MONGO_USER = 'root'
# MONGO_PWD = '123456aA@'
# MONGO_AUTH_DB = 'xsocial'
# MONGO_COLLECTION = 'articles'

# KAFKA
KAFKA_HOST = 'localhost'
KAFKA_PORT = 9092
ARTICLE_TOPIC = 'crawler-news-post'

# POSTGRESQL
# POSTGRES_HOST = 'mh-x1'
# POSTGRES_PORT = 5431
# POSTGRES_USER = 'postgres'
# POSTGRES_PASSWORD = '123'
# POSTGRES_DB = 'mh'

# POSTGRESQL
POSTGRES_HOST = 'localhost'
POSTGRES_PORT = 5320
POSTGRES_USER = 'root'
POSTGRES_PASSWORD = 'root'
POSTGRES_DB = 'social'

#
REACTOR_THREADPOOL_MAXSIZE = 1000
DOWNLOAD_TIMEOUT = 60
AJAXCRAWL_ENABLED = True

# Log
LOG_LEVEL = 'INFO'

# DATE_FORMAT
DATE_FORMAT = (
    '%H:%M %d/%m/%Y',
    '%H:%M:%S %d/%m/%Y',
    '%H:%M, %d/%m/%Y',
    'Cập nhật lúc %H:%M %d/%m/%Y',
    'Ngày cập nhật %d/%m/%Y',
    'Cập nhật ngày: %d/%m/%Y %H:%m (GMT +7)',
    'Đăng lúc: %d/%m/%Y %H:%m (GMT+7)',
    '%d/%m/%Y %H:%m (GMT+7)',
    'Xuất bản: %H:%m %d/%m/%Y [GMT+7]',
    'Ngày đăng: %d-%m-%Y, %H:%m',
    'Cập nhật: %H:%m %d-%m-%Y',
    '%H:%m %d/%m/%Y GMT+7',
    '%d.%m.%Y %H:%M:%S',
    '%d/%m/%Y %H:%M:%S',
    '%d/%m/%Y %H:%M',
    '%d/%m/%Y, %H:%M',
    '%d-%m-%Y %H:%m',
    '%d-%m-%Y %H:%M:%S%z',
    '%Y-%m-%d %H:%M:%S',
    '%Y-%m-%dICT%H:%M:%S',
    '%Y-%m-%dT%H:%M:%S%z',
    '%Y-%m-%dT%H:%M:%S.%f',
    '%Y-%m-%dT%H:%M:%S',
    '%Y-%m-%dT%H:%M:%S.%f%z',
    '%Y-%m-%dT%H:%M:%ST%z',
    '%H:%m | %d/%m/%Y |',
    '| %d/%m/%Y, %H:%M:%S',
    '%I:%M %p %d/%m/%Y',  # 03:01 PM 18/11/2020
    '%d/%m/%Y %I:%M:%S %p',
    '%d-%m-%Y - %I:%M %p',
    '%I:%M %p, %d/%m/%Y'
)
