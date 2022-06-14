import re
from urllib.parse import urlparse

from news_bot.utils.utils import HelperFunction


class DataNormalizePipeline:
    def __init__(self):
        self.resources = []

    def process_item(self, item, spider):
        # domain
        domain = item['domain']
        normalized_domain = HelperFunction.normalize_url(item['domain'])
        item['domain'] = normalized_domain

        scheme = urlparse(item['raw_url']).scheme

        # title
        item['title'] = HelperFunction.normalize_content(item['title'])

        # content
        item['content'] = HelperFunction.normalize_content(item['content'])

        # summary
        item['summary'] = HelperFunction.normalize_content(item['summary'])

        # share_content
        normalized_content = []
        for sc_resource in item['share_content']:
            if sc_resource == '':
                continue
            if sc_resource.startswith('//'):
                sc_resource = sc_resource.replace('//', '')
            if sc_resource.startswith('/'):
                sc_resource = f'{domain}{sc_resource}'
            if 'https://' not in sc_resource and 'http://' not in sc_resource:
                sc_resource = f'{scheme}://{sc_resource}'
            normalized_content.append(sc_resource)
        item['share_content'] = normalized_content

        # images
        normalized_images = []
        for img_resource in item['image_sources']:
            img_resource = img_resource.strip()
            if img_resource == '':
                continue
            if img_resource.startswith('//'):
                img_resource = img_resource.replace('//', '')
            if img_resource.find(domain) == -1:
                if img_resource.startswith('/'):
                    img_resource = f'{domain}{img_resource}'
                if 'https://' not in img_resource and 'http://' not in img_resource:
                    img_resource = f'{scheme}://{img_resource}'
            else:
                if img_resource.startswith('/') or img_resource.startswith('//'):
                    img_resource = f'{scheme}:{img_resource}'
            normalized_images.append(img_resource)
        item['image_sources'] = normalized_images

        # videos
        normalized_videos = []
        for vid_resource in item['video_sources']:
            if vid_resource == '':
                continue
            if vid_resource.startswith('//'):
                vid_resource = vid_resource.replace('//', '')
            if vid_resource.startswith('/'):
                vid_resource = f'{domain}{vid_resource}'
            if 'https://' not in vid_resource and 'http://' not in vid_resource:
                vid_resource = f'{scheme}://{vid_resource}'
            normalized_videos.append(vid_resource)
        item['video_sources'] = normalized_videos

        # author
        author = item['author_display_name']
        if author is None or (author and author.find("All about Football") != -1):
            author = ''
        item['author_display_name'] = HelperFunction.normalize_author_display_name(author)

        # raw_html
        raw_html = []
        raw_html_parsed = item['raw_html']
        for raw_item in raw_html_parsed:
            raw_item_norm = HelperFunction.normalize_raw_content(raw_item)
            if raw_item_norm != "":
                raw_html.append(raw_item_norm)
        item['raw_html'] = "".join(raw_html)

        # tag
        tag = item['tag']
        list_tags_normalize = []
        if tag and isinstance(tag, str):
            list_tags = re.split('[,;]', tag)
            for tag_item in list_tags:
                tag_item_norm = HelperFunction.normalize_tag(tag_item)
                if tag_item_norm != "":
                    list_tags_normalize.append(tag_item_norm)

        item['tag'] = list_tags_normalize

        return item
