# -*- coding: utf-8 -*-

import sys
import requests as pyRequests
import analyze_spark_param_tools
from flask import Flask, request, render_template, redirect
from math import ceil

app = Flask(__name__)

# -------------------- ANALYZE SPARK PARAM TOOLS -------------------- #

# spark param tools index page
@app.route('/analyze/spark/param_tools/')
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

# spark param tools submit page
@app.route('/analyze/spark/param_tools/submit/', methods=['POST'])
def spark_param_tools_submit():
    if request.form.get('appid') is not None:
        app_id = request.form['appid']
        pyRequests.post(analyze_spark_param_tools.config.GetServletURL(),
                        data={'appid': app_id})
        return redirect('/analyze/spark/param_tools')
    else:
        return u"缺少参数"

# spark param tools retrieve page
@app.route('/analyze/spark/param_tools/retrieve/', methods=['GET'])
def spark_param_tools_retrieve():
    if request.args.get('progressid') is not None:
        progressid = request.args['progressid']
        client = analyze_spark_param_tools.ProgressClient()
        item = client.GetItemByProgressId(progressid)
        client.Close()
        return render_template('retrieve.html', item=item)
    else:
        return u"缺少参数"


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print "usage: python main.py <config file>"
        sys.exit(1)
        
    config_path = sys.argv[1]
    analyze_spark_param_tools.config.load(config_path)
    app.run(host='0.0.0.0', debug=True, port=5001)
