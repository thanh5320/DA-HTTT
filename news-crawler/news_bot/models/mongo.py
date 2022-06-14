import os
import sys

from pymongo import MongoClient
import yaml


class Mongo:
    __instance = None
    connection_str = None

    @staticmethod
    def getInstance():
        if Mongo.__instance is None:
            Mongo()
        return Mongo.__instance

    def __init__(self):
        if Mongo.__instance is not None:
            raise Exception("This class is a singleton!")
        else:
            try:
                root_dir = os.path.abspath(os.path.dirname(__file__))
                yaml_path = os.path.join(root_dir, '../../config/config.yaml')
                with open(yaml_path, "r") as ymlfile:
                    cfg = yaml.safe_load(stream=ymlfile)

                # mongo_uri = 'mongodb://james:brewer@localhost/test'
                client = MongoClient(f"mongodb://{cfg['mongo']['user']}:{cfg['mongo']['password']}@{cfg['mongo']['host']}:{cfg['mongo']['port']}/{cfg['mongo']['database']}")
            except Exception as e:
                print(f"got exception while init mongo - {str(e)}")
                sys.exit()
            Mongo.__instance = self
