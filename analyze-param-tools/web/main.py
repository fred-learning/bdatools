# -*- coding: utf-8 -*-

import sys
import requests as pyRequests
import analyze_spark_param_tools
from flask import Flask, request, render_template, redirect
from math import ceil

app = Flask(__name__)

# -------------------- ANALYZE SPARK PARAM TOOLS -------------------- #


# 参数推荐工具首页
@app.route('/')
def spark_param_tools_index():
    startStr = request.args.get('startIdx', None)
    startIdx = int(startStr) \
               if analyze_spark_param_tools.is_integer(startStr) else 0
    client = analyze_spark_param_tools.ProgressClient()
    items = client.GetRecentItems(startIdx)
    pageCount = int(ceil(client.GetItemCount() / 10.0))
    activePage = int(ceil( (startIdx + 1) / 10.0 ))
    client.Close()
    return render_template('main.html', items=items,
                           pageCount=pageCount, activePage=activePage)


# 提交参数推荐任务
@app.route('/recommend_submit/', methods=['POST'])
def spark_param_tools_submit():
    if request.form.get('appid') is not None:
        app_id = request.form['appid']
        pyRequests.post(analyze_spark_param_tools.config.GetRecommendServletURL(),
                        data={'appid': app_id})
        return redirect('/')
    else:
        return u"缺少参数"


# 查看参数推荐任务结果
@app.route('/recommend_retrieve/', methods=['GET'])
def spark_param_tools_retrieve():
    if request.args.get('progressid') is not None:
        progressid = request.args['progressid']
        client = analyze_spark_param_tools.ProgressClient()
        item = client.GetItemByProgressId(progressid)
        client.Close()
        return render_template('recommend_retrieve.html', item=item,
                               run_params_path=analyze_spark_param_tools.config.GetRunParamServletURL())
    else:
        return u"缺少参数"


# 尝试运行推荐参数
@app.route('/run_params/', methods=['POST'])
def spark_run_param():
    progressid = request.form.get('progressid')
    appid = request.form.get('appid')
    params = request.form.get('params')
    if progressid is not None and appid is not None and params is not None:
        data = {'progressid': progressid, 'appid': appid, 'params': params}
        pyRequests.post(analyze_spark_param_tools.config.GetRunParamServletURL(),
                        data=data)
        return redirect('/run_param_history/')
    else:
        return u"缺少参数"

# 推荐参数运行历史
@app.route('/run_param_history/', methods=['GET'])
def spark_run_param_history():
    resp = pyRequests.get(analyze_spark_param_tools.config.GetRunParamHistoryServletURL())
    json_obj = resp.json()
    page_count = max(1, int(ceil(len(json_obj) / 10.0)))
    page_idx_str = request.args.get('pageIdx', '1')
    page_idx = max(1, min(int(page_idx_str), page_count))
    start_idx = (page_idx - 1) * 10
    end_idx = start_idx + 10
    items = [item for item in json_obj[start_idx:end_idx]]
    return render_template('run_param_history.html', items=items,
                           pageCount=page_count, activePage=page_idx,
                           run_params_log_path=analyze_spark_param_tools.config.GetRunParamLogServletURL())

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print "usage: python main.py <config file>"
        sys.exit(1)
        
    config_path = sys.argv[1]
    analyze_spark_param_tools.config.load(config_path)
    app.run(host='0.0.0.0', debug=True, port=5001)
