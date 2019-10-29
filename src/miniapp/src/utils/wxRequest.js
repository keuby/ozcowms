
export default {
  defaults: {
    baseURL: '',
    headers: {
      'content-type': 'application/json',
    },
  },
  request(options) {
    const { url, config, ...otherOptions } = options
    const absoluteUrl = (config.baseURL || this.defaults.baseURL) + url
    let header = config.json === false ? {
      ...this.defaults.headers,
      'content-type': 'application/x-www-form-urlencoded'
    } : this.defaults.headers
    const mergedOptions = {
      header,
      url: absoluteUrl,
      ...otherOptions,
    }
    return new Promise((resolve, reject) => {
      wx.request({
        ...mergedOptions,
        success: (resp) => {
          if (resp.statusCode < 400) {
            resolve(resp.data)
          } else {
            reject(resp)
          }
        },
        fail: resp => reject(resp),
      })
    })
  },
  get(url, data, config = {}) {
    const options = {
      url, data, method: 'GET', config,
    }
    return this.request(options)
  },
  post(url, data, config = {}) {
    const options = {
      url, data, method: 'POST', config,
    }
    return this.request(options)
  },
  put(url, data, config = {}) {
    const options = {
      url, data, method: 'PUT', config,
    }
    return this.request(options)
  },
  delete(url, data, config = {}) {
    const options = {
      url, data, method: 'DELETE', config,
    }
    return this.request(options)
  },
}