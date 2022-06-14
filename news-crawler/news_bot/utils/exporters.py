from scrapy.exporters import CsvItemExporter


class CsvCustomSeparator(CsvItemExporter):
    def __init__(self, *args, **kwargs):
        kwargs['encoding'] = 'utf-8'
        kwargs['delimiter'] = '\t'
        super(CsvCustomSeparator, self).__init__(*args, **kwargs)
