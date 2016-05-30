# -*- coding: utf-8 -*-

import sys
import json
import requests as pyRequests
from analyze_spark_param_tools import config
from flask import Flask, request, render_template, redirect

app = Flask(__name__)
config.load("../settings.properties")


# 参数推荐历史记录
@app.route(config.get_recommend_progress_view_servlet_relative_url(), methods=['GET'])
def recommend_progress_view():
    pageIdx = request.args.get('pageIdx', 1)
    resp = pyRequests.get(config.get_recommend_progress_view_servlet_absolute_url(),
                          params={'pageIdx': pageIdx})
    if resp.status_code != pyRequests.codes.ok:
        return u"网络出错"

    items_json = resp.json()
    if items_json['value'] != 0:
        return items_json['message']

    items = [item for item in items_json['items']]
    for item in items:
        if len(item['result']) > 0:
            item['result'] = json.loads(item['result'])

    activePage = items_json['activePage']
    pageCount = items_json['pageCount']
    pageRange = range(max(1, activePage - 3), min(activePage + 4, pageCount + 1))
    return render_template('recommend_progress_view.html', items=items,
                           config=config, pageCount=pageCount,
                           activePage=activePage, pageRange=pageRange)

# 参数推荐任务提交
@app.route(config.get_recommend_job_submit_servlet_relative_url(), methods=['POST'])
def recommend_progress_job_submit():
    appid = request.form.get('appid')
    resp = pyRequests.post(config.get_recommend_job_submit_servlet_absolute_url(),
                          data={'appid': appid})
    if resp.status_code != pyRequests.codes.ok:
        return u'网络出错'
    resp_json = resp.json()

    if resp_json['value'] != 0:
        return resp_json['message']

    items = [item for item in resp_json]
    return redirect(config.get_recommend_progress_view_servlet_relative_url())

# 查看参数推荐任务结果
@app.route(config.get_recommend_progress_detail_servlet_relative_url(), methods=['GET'])
def spark_param_tools_retrieve():
    progressid = request.args.get('progressid')
    resp = pyRequests.get(config.get_recommend_progress_detail_servlet_absolute_url(),
                          params={'progressid': progressid})
    if resp.status_code != pyRequests.codes.ok:
        return u'网络出错'
    resp_json = resp.json()

    if resp_json['value'] != 0:
        return resp_json['message']

    abstract = {'progressid': resp_json['item']['progressid'],
                'appid': resp_json['item']['appid'],
                'startTime': resp_json['item']['startTime'],
                'endTime': resp_json['item']['endTime']}
    if 'recommendItemList' in json.loads(resp_json['item']['result']):
        itemList = json.loads(resp_json['item']['result'])['recommendItemList']
        items = [item for item in itemList]
    else:
        items = []
    return render_template('recommend_progress_detail.html', abstract=abstract,
                           items=items, config=config)

# 运行经过优化的参数
@app.route(config.get_run_param_submit_servlet_relative_url(), methods=['POST'])
def spark_run_param():
    appid = request.form.get('appid')
    params = request.form.get('params')
    data = {'appid': appid, 'params': params}
    resp = pyRequests.post(config.get_run_param_submit_servlet_absolute_url(), data=data)

    if resp.status_code != pyRequests.codes.ok:
        return u'网络出错'
    resp_json = resp.json()

    if resp_json['value'] != 0:
        return resp_json['message']

    return redirect(config.get_run_param_history_servlet_relative_url())


# 运行经过优化的参数运行历史
@app.route(config.get_run_param_history_servlet_relative_url(), methods=['GET'])
def spark_run_param_history():
    pageIdx = request.args.get('pageIdx', 1)
    resp = pyRequests.get(config.get_run_param_history_servlet_absolute_url(),
                          params={'pageIdx': pageIdx})

    if resp.status_code != pyRequests.codes.ok:
        return u"网络出错"

    items_json = resp.json()
    if items_json['value'] != 0:
        return items_json['message']

    activePage = items_json['activePage']
    pageCount = items_json['pageCount']
    pageRange = range(max(1, activePage - 3), min(activePage + 4, pageCount + 1))
    items = [item for item in items_json['items']]
    return render_template('run_param_history.html', items=items, config=config,
                           pageCount=pageCount, activePage=activePage,
                           pageRange=pageRange)

# 历史数据库, view
@app.route(config.get_historydb_view_servlet_relative_url(), methods=['GET'])
def history_db_view():
    pageIdx = request.args.get('pageIdx', 1)
    resp = pyRequests.get(config.get_historydb_view_servlet_absolute_url(),
                          params={'pageIdx': pageIdx})
    if resp.status_code != pyRequests.codes.ok:
        return u"网络出错"

    items_json = resp.json()
    if items_json['value'] != 0:
        return items_json['message']

    items = [item for item in items_json['viewItems']]
    activePage = items_json['activePage']
    pageCount = items_json['pageCount']
    pageRange = range(max(1, activePage - 3), min(activePage + 4, pageCount + 1))
    return render_template('history_db.html', items=items,
                           config=config, pageCount=pageCount,
                           activePage=activePage, pageRange=pageRange)


# 历史数据库, delete
@app.route(config.get_historydb_delete_servlet_relative_url(), methods=["POST"])
def history_db_delete():
    appid = request.form.get('appid')
    activePage = request.form.get('activePage', 1)
    resp = pyRequests.post(config.get_historydb_delete_servlet_absolute_url(),
                          data={'appid': appid})
    if resp.status_code != pyRequests.codes.ok:
        return u'网络出错'
    resp_json = resp.json()

    if resp_json['value'] != 0:
        return resp_json['message']

    url = '%s?pageIdx=%s' % (config.get_historydb_view_servlet_relative_url(),
                             activePage)
    return redirect(url)


# 历史数据库, add
@app.route(config.get_historydb_add_servlet_relative_url(), methods=["POST"])
def history_db_add():
    appid = request.form.get('appid')
    resp = pyRequests.post(config.get_historydb_add_servlet_absolute_url(),
                           data={'appid': appid})
    if resp.status_code != pyRequests.codes.ok:
        return u'网络出错'
    resp_json = resp.json()

    if resp_json['value'] != 0:
        return resp_json['message']
    return redirect(config.get_historydb_view_servlet_relative_url())


if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True, port=5001)