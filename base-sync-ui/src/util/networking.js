import Vue from 'vue'


export default {

    // basicUrl: 'http://127.0.0.1',
    basicUrl: '',

    httpMethod: {
        GET: 'get',
        POST: 'post',
        PUT: 'put',
        DELETE: 'delete',
    },

    doGet(url, options = null) {
        return this.doRequest(url, this.httpMethod.GET, options);
    },

    doPost(url, body = null, options = null) {
        return this.doRequest(url, this.httpMethod.POST, body, options);
    },

    doPut(url, body = null, options = null) {
        return this.doRequest(url, this.httpMethod.PUT, body, options);
    },

    doDelete(url, body = null, options = null) {
        return this.doRequest(url, this.httpMethod.DELETE, null, options);
    },


    doRequest(url, method, body, options) {

        let request = null;
        url = this.basicUrl + url;


        switch (method) {

            case this.httpMethod.GET: {
                request = Vue.http.get(url, body);
                break;
            }

            case this.httpMethod.POST: {
                request = Vue.http.post(url, body, options);
                break;
            }

            case this.httpMethod.PUT: {
                request = Vue.http.put(url, body, options);
                break;
            }

            case this.httpMethod.DELETE: {
                request = Vue.http.delete(url, options);
                break;
            }


            default: {
                break;
            }

        }

        return this.requestPromise(request);
    },


    requestPromise(request) {



        return request.then((response) => {

                if (response.status === 200) {


                    if (response.body.code === 200) {

                        // 成功回调
                        return Promise.resolve(response.body);
                    }

                    // 失败回调
                    return Promise.reject(response.body);
                }

                return Promise.reject(response.statusText);
            },
            response => Promise.reject(response.statusText))
    },


};