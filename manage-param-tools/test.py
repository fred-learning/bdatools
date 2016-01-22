
import json

with open('./config/allow_params.json') as f:
    json_obj = json.load(f)

print json_obj['cluster-status']['cassandra']
