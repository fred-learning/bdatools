
import json
from bson import json_util
import pymongo
from pyjavaproperties import Properties
from flask import render_template, redirect, request
import requests as Requests


class Config:
    def __init__(self):
        p = Properties()
        self.properties_ = p

    def load(self, path):
        self.properties_.load(open(path))

    def GetMongoURL(self):
        return 'mongodb://%s:%s/' % (self.properties_['mongoIP'],
                                     self.properties_['mongoPort'])

    def GetMongoDBName(self):
        return self.properties_['mongoDBName']

    def GetMongoDBCollection(self):
        return self.properties_['mongoProgressCollection']

    def GetRecommendServletURL(self):
        return 'http://%s:%s%s' % (self.properties_['jettyIP'],
                                   self.properties_['jettyPort'],
                                   self.properties_['recommendServletPath'])

    def GetRunParamHistoryServletURL(self):
        return 'http://%s:%s%s' % (self.properties_['jettyIP'],
                                   self.properties_['jettyPort'],
                                   self.properties_['runParamHistoryServletPath'])

    def GetRunParamServletURL(self):
        return 'http://%s:%s%s' % (self.properties_['jettyIP'],
                                   self.properties_['jettyPort'],
                                   self.properties_['runParamServletPath'])

    def GetRunParamLogServletURL(self):
        return 'http://%s:%s%s' % (self.properties_['jettyIP'],
                                   self.properties_['jettyPort'],
                                   self.properties_['runParamLogServletPath'])


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

    def GetItemByProgressId(self, progressid):
        item = self.col_.find_one({'progressid': progressid})
        ret = json.loads(json_util.dumps(item))
        ret['result'] = json.loads(ret['result'])
        return ret

            
def is_integer(num):
    try:
        int(num)
        return True
    except:
        return False

                                           
config = Config()            
