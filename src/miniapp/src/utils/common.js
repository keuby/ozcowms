/* eslint-disable no-extend-native */
Function.prototype.once = function () {
  let __self = this
  let called = false
  return function () {
    if (!called) {
      called = true
      let ret = __self.apply(this, arguments)
      if (isPromise(ret)) {
        ret.then(() => { called = false })
      } else {
        called = false
      }
      return ret
    }
  }
}

function wxApiPromisify (object, name) {
  return (param = {}, ...args) => {
    return new Promise((resolve, reject) => {
      object[name]({
        ...param,
        success: res => {
          resolve(res)
          param.success && param.success(res)
        },
        fail: res => {
          reject(res)
          param.fail && param.fail(res)
        }
      })
    }, ...args)
  }
}

export function mapWxApi (names = []) {
  let result = {}, target = wx || {}
  for (let name of names) {
    if (name in target) {
      result[name] = wxApiPromisify(target, name)
    }
  }
  return result
}

export function isPromise (val) {
  return (
    isDef(val) &&
    typeof val.then === 'function' &&
    typeof val.catch === 'function'
  )
}

export function isDef (v) {
  return v !== undefined && v !== null
}

export function isObject (obj) {
  return obj !== null && typeof obj === 'object'
}

export function defer () {
  let p = {}
  p.promise = new Promise((resolve, reject) => {
    p.resolve = resolve
    p.reject = reject
  })
  return p
}

export function looseEqual(a, b) {
  if (a === b) return true
  const isObjectA = isObject(a)
  const isObjectB = isObject(b)
  if (isObjectA && isObjectB) {
    try {
      const isArrayA = Array.isArray(a)
      const isArrayB = Array.isArray(b)
      if (isArrayA && isArrayB) {
        return a.length === b.length && a.every((e, i) => {
          return looseEqual(e, b[i])
        })
      } else if (a instanceof Date && b instanceof Date) {
        return a.getTime() === b.getTime()
      } else if (!isArrayA && !isArrayB) {
        const keysA = Object.keys(a)
        const keysB = Object.keys(b)
        return keysA.length === keysB.length && keysA.every(key => {
          return looseEqual(a[key], b[key])
        })
      } else {
        return false
      }
    } catch (e) {
      return false
    }
  } else if (!isObjectA && !isObjectB) {
    return String(a) === String(b)
  } else {
    return false
  }
}

export function arrayPlain(array) {
  let plain = []
  for (let element of array) {
    if (Array.isArray(array)) {
      plain = plain.concat(arrayPlain(element))
    } else {
      plain.push(element)
    }
  }
  return plain
}

export const noop = () => {}