import os
import sys
from urllib.parse import urlparse

import psycopg2
import yaml

from news_bot.utils.utils import HelperFunction


class Postgresql:
    __instance = None
    connection_str = None

    @staticmethod
    def getInstance():
        if Postgresql.__instance is None:
            Postgresql()
        return Postgresql.__instance

    def __init__(self):
        """ Virtually private constructor. """
        if Postgresql.__instance is not None:
            raise Exception("This class is a singleton!")
        else:
            try:
                root_dir = os.path.abspath(os.path.dirname(__file__))
                yaml_path = os.path.join(root_dir, '../../config/config.yaml')
                with open(yaml_path, "r") as ymlfile:
                    cfg = yaml.safe_load(stream=ymlfile)

                self.connection_str = "host={} port={} dbname={} user={} password={}".format(cfg['postgresql']['host'],
                                                                                             cfg['postgresql']['port'],
                                                                                             cfg['postgresql'][
                                                                                                 'database'],
                                                                                             cfg['postgresql']['user'],
                                                                                             cfg['postgresql'][
                                                                                                 'password'])
            except Exception as error:
                print(f"got exception while init postgresql {str(error)}")
                sys.exit()
            Postgresql.__instance = self

    def check_postgres_data(self, data):
        list_data = data.get('domains')
        if not list_data or len(list_data) == 0:
            return

        for item in list_data:
            domain = HelperFunction.normalize_url(item['domain'])
            scheme = urlparse(item['domain']).scheme
            existed = self.get_domain_by_name(domain)
            if existed is not None:
                print(existed)
                self.update_domain_info(domain, item, scheme)
            else:
                self.insert(scheme=scheme,
                            domain=domain,
                            all_links=HelperFunction.normalize_xpath(item['all_links']),
                            all_subs=HelperFunction.normalize_xpath(item['all_subs']),
                            next_page=HelperFunction.normalize_xpath(item['next_page']),
                            content=HelperFunction.normalize_xpath(item['content']),
                            image_sources=HelperFunction.normalize_xpath(item['image_sources']),
                            video_sources=HelperFunction.normalize_xpath(item['video_sources']),
                            author=HelperFunction.normalize_xpath(item['author_display_name']),
                            tags=HelperFunction.normalize_xpath(item['tags']),
                            published_time=HelperFunction.normalize_xpath(item['published_time']),
                            share_content=HelperFunction.normalize_xpath(item['share_content']),
                            raw_html=HelperFunction.normalize_xpath(item['raw_html']) if 'raw_html' in item else '')

    def insert(self, scheme, domain, all_links, all_subs, next_page, content, image_sources, video_sources, author,
               tags,
               published_time, share_content, raw_html):
        con = None
        mycursor = None
        try:
            con = psycopg2.connect(self.connection_str)
            mycursor = con.cursor()

            insert_query = """
                INSERT INTO crawler.domain(domain,all_links,all_subs,next_page,content,image_sources,video_sources,author_display_name,tags,published_time,share_content,raw_html,scheme)
                VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)
            """

            record_to_insert = (domain, all_links, all_subs, next_page, content, image_sources,
                                video_sources, author, tags, published_time, share_content, raw_html, scheme)
            mycursor.execute(insert_query, record_to_insert)
            con.commit()
        except Exception as error:
            print(error)
        finally:
            if mycursor is not None:
                mycursor.close()
            if con is not None:
                con.close()

    def get_crawlable_domains(self, offset, limit):
        result = None
        con = None
        mycursor = None
        try:
            con = psycopg2.connect(self.connection_str)
            mycursor = con.cursor()
            mycursor.execute("SELECT * FROM crawler.domain WHERE all_links IS NOT NULL AND all_subs IS NOT NULL LIMIT {} OFFSET {}".format(limit, offset))
            result = mycursor.fetchall()
            return result
        except Exception as error:
            print(error)
        finally:
            if mycursor is not None:
                mycursor.close()
            if con is not None:
                con.close()
        return result

    def get_list_domains(self, limit, offset):
        result = None
        con = None
        mycursor = None
        try:
            con = psycopg2.connect(self.connection_str)
            mycursor = con.cursor()
            mycursor.execute("SELECT * FROM crawler.domain LIMIT {} OFFSET {}".format(limit, offset))
            result = mycursor.fetchall()
            return result
        except Exception as error:
            print(error)
        finally:
            if mycursor is not None:
                mycursor.close()
            if con is not None:
                con.close()
        return result

    def get_domain_by_name(self, name):
        result = None
        con = None
        mycursor = None
        try:
            con = psycopg2.connect(self.connection_str)
            mycursor = con.cursor()
            mycursor.execute("SELECT * FROM crawler.domain WHERE domain LIKE '%{}%'".format(name))
            result = mycursor.fetchone()
            return result
        except Exception as error:
            print(error)
        finally:
            if mycursor is not None:
                mycursor.close()
            if con is not None:
                con.close()
        return result

    def update_domain_info(self, name, xpaths, scheme):
        con = None
        mycursor = None
        try:
            con = psycopg2.connect(self.connection_str)
            mycursor = con.cursor()

            update_query = """
                UPDATE crawler.domain SET all_links=%s,all_subs=%s,next_page=%s,content=%s,image_sources=%s,
                video_sources=%s,author_display_name=%s,tags=%s,published_time=%s,share_content=%s,raw_html=%s,scheme=%s
                WHERE domain LIKE %s
            """
            record_to_update = (xpaths['all_links'], xpaths['all_subs'], xpaths['next_page'], xpaths['content'],
                                xpaths['image_sources'], xpaths['video_sources'], xpaths['author_display_name'],
                                xpaths['tags'], xpaths['published_time'], xpaths['share_content'], xpaths['raw_html'],
                                scheme, name)

            mycursor.execute(update_query, record_to_update)
            con.commit()

            row_count = mycursor.rowcount
            print(f"{row_count} has been updated - domain {name}")

        except Exception as error:
            print(error)
        finally:
            if mycursor is not None:
                mycursor.close()
            if con is not None:
                con.close()
