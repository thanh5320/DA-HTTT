import os
import sys

import grpc
import yaml

from services.proto import stringbloom_pb2
from services.proto import stringbloom_pb2_grpc


class StringBloomClient:
    __instance = None
    channel = None
    stub = None

    @staticmethod
    def getInstance():
        """ Static access method. """
        if StringBloomClient.__instance is None:
            StringBloomClient()
        return StringBloomClient.__instance

    def __init__(self):
        """ Virtually private constructor. """
        if StringBloomClient.__instance is not None:
            raise Exception("This class is a singleton!")
        else:
            try:
                cfg = None
                root_dir = os.path.abspath(os.path.dirname(__file__))
                yaml_path = os.path.join(root_dir, '../../config/config.yaml')
                with open(yaml_path, "r") as ymlfile:
                    cfg = yaml.safe_load(stream=ymlfile)

                self.channel = grpc.insecure_channel(
                    '{}:{}'.format(cfg['services']['bloom_filter']['host'], cfg['services']['bloom_filter']['port']))
                self.stub = stringbloom_pb2_grpc.StringBloomServiceStub(self.channel)
            except Exception as e:
                print(str(e))
                print("got exception while init ")
                sys.exit()
            StringBloomClient.__instance = self

    def check_exists(self, str, timestamp):
        request = stringbloom_pb2.CheckExistsRequest()
        request.string = str
        request.timestamp = timestamp
        response = self.stub.CheckExists(request)
        return response.exists

    def check_exists_and_set(self, str, timestamp):
        request = stringbloom_pb2.CheckExistsRequest()
        request.string = str
        request.timestamp = timestamp
        response = self.stub.CheckExistsAndSet(request)
        return response.exists
