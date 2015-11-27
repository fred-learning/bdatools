# -*- coding: utf-8 -*-

from flask import Flask, render_template, redirect, request
from math import ceil
import json
import pymongo
import requests as Requests

class ProgressClient:
    def __init__(self):
        self.client_ = pymongo.MongoClient('192.168.3.57', 27017)
        self.db_ = self.client_['spark']
        self.col_ = self.db_['recommend_progress']

    def recent_items(self, startIdx=0, num=10):
        sorted_result = self.col_.find({}).sort("_id",
                                                direction=pymongo.DESCENDING)
        limit_result = sorted_result.skip(startIdx).limit(num)
        items = []
        for item in limit_result:
            if len(item['result']) > 0:
                item['result'] = json.loads(item['result'])
            items.append(item)
        return items

    def item_count(self):
        return self.col_.count({})

    def close(self):
        if self.client_ is not None:
            self.client_.close()

            
def is_integer(num):
    try:
        int(num)
        return True
    except:
        return False
    

app = Flask(__name__)

@app.route('/')
def index():
    startStr = request.args.get('startIdx', None)
    startIdx = int(startStr) if is_integer(startStr) else 0
    client = ProgressClient()
    items = client.recent_items(startIdx)
    page_count = int(ceil(client.item_count() / 10.0))
    active_page = int(ceil( (startIdx + 1) / 10.0 ))
    client.close()
    return render_template('index.html', items=items,
                           page_count=page_count, active_page=active_page)

@app.route('/submit/', methods=['POST'])
def submit():
    if request.form.get('clusterid') is not None\
       and request.form.get('appid') is not None:
        cluster_id = request.form['clusterid']
        app_id = request.form['appid']
        Requests.post('http://127.0.0.1:8890/sparkrecommend',
                      data={'appid': app_id})
        return redirect('/')
    else:
        return u"缺少参数"

if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True)
