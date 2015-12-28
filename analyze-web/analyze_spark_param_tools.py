
import json
import pymongo
from pyjavaproperties import Properties
from flask import render_template, redirect, request
import requests as Requests
import settings

class Config:
    def __init__(self):
        p = Properties()
        p.load(open(settings.ANALYZE_SPARK_PARAM_TOOLS_SETTINGS))
        self.properties_ = p

    def GetMongoURL(self):
        return 'mongodb://%s:%s/' % (self.properties_['mongoIP'],
                                     self.properties_['mongoPort'])

    def GetMongoDBName(self):
        return self.properties_['mongoDBName']

    def GetMongoDBCollection(self):
        return self.properties_['mongoProgressCollection']

    def GetServletURL(self):
        return 'http://192.168.3.57:%s%s' % (self.properties_['jettyPort'],
                                              self.properties_['servletPath'])
    
class ProgressClient:
    def __init__(self):
        self.client_ = pymongo.MongoClient(config.GetMongoURL())
        self.db_ = self.client_[config.GetMongoDBName()]
        self.col_ = self.db_[config.GetMongoDBCollection()]

    def GetRecentItems(self, startIdx=0, num=10):
        sortedResult = self.col_.find({}).sort("_id",
                                               direction=pymongo.DESCENDING)
        limitResult = sortedResult.skip(startIdx).limit(num)
        items = []
        for item in limitResult:
            if len(item['result']) > 0:
                item['result'] = json.loads(item['result'])
            items.append(item)
        return items

    def GetItemCount(self):
        return self.col_.count({})

    def Close(self):
        if self.client_ is not None:
            self.client_.close()

            
def is_integer(num):
    try:
        int(num)
        return True
    except:
        return False

                                           
config = Config()            
