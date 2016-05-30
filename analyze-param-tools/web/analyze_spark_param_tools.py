
from pyjavaproperties import Properties


class Config:
    def __init__(self):
        p = Properties()
        self.properties_ = p

    def load(self, path):
        self.properties_.load(open(path))

    def get_mongodb_url(self):
        return 'mongodb://%s:%s/' % (self.properties_['mongoIP'],
                                     self.properties_['mongoPort'])

    def get_mongodb_name(self):
        return self.properties_['mongoDBName']

    def get_mongodb_collection(self):
        return self.properties_['mongoProgressCollection']

    def get_servlet_prefix(self):
        return 'http://%s:%s' % (self.properties_['jettyIP'],
                                 self.properties_['jettyPort'])

    # recommend
    def get_recommend_progress_view_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['recommendProgressViewServletPath'])

    def get_recommend_progress_detail_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['recommendProgressDetailServletPath'])

    def get_recommend_job_submit_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['recommendJobSubmitServletPath'])

    def get_recommend_progress_view_servlet_relative_url(self):
        return self.properties_['recommendProgressViewServletPath']

    def get_recommend_progress_detail_servlet_relative_url(self):
        return self.properties_['recommendProgressDetailServletPath']

    def get_recommend_job_submit_servlet_relative_url(self):
        return self.properties_['recommendJobSubmitServletPath']

    # run param
    def get_run_param_history_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['runParamHistoryServletPath'])

    def get_run_param_submit_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['runParamSubmitServletPath'])

    def get_run_param_log_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['runParamLogServletPath'])

    def get_run_param_history_servlet_relative_url(self):
        return self.properties_['runParamHistoryServletPath']

    def get_run_param_submit_servlet_relative_url(self):
        return self.properties_['runParamSubmitServletPath']

    def get_run_param_log_servlet_relative_url(self):
        return self.properties_['runParamLogServletPath']

    # history db
    def get_historydb_view_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['historyDBViewServletPath'])

    def get_historydb_add_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['historyDBAddServletPath'])

    def get_historydb_delete_servlet_absolute_url(self):
        return '%s%s' % (self.get_servlet_prefix(),
                         self.properties_['historyDBDeleteServletPath'])

    def get_historydb_view_servlet_relative_url(self):
        return self.properties_['historyDBViewServletPath']

    def get_historydb_add_servlet_relative_url(self):
        return self.properties_['historyDBAddServletPath']

    def get_historydb_delete_servlet_relative_url(self):
        return self.properties_['historyDBDeleteServletPath']


    def get_spark_history_url(self):
        return self.properties_['historyServerPath']

config = Config()
