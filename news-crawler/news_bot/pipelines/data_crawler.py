from scrapy.dupefilters import RFPDupeFilter
from scrapy.exceptions import DropItem


class NewsCrawlerPipeline(RFPDupeFilter):

    def process_item(self, item, spider):
        if item['raw_url'] in self.fingerprints:
            raise DropItem(f"Duplicate url found: {item['raw_url']}")
        else:
            self.fingerprints.add(item['raw_url'])
            return item
