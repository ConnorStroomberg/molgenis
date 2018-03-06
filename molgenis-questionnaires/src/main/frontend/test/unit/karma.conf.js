var webpackConfig = require('../../build/webpack.test.conf')

module.exports = function (config) {
  config.set({
    browsers: ['PhantomJS'],
    frameworks: ['mocha', 'sinon-chai'],
    reporters: ['spec', 'coverage'],
    files: [
      '../../node_modules/es6-promise/dist/es6-promise.auto.js',
      '../../node_modules/babel-polyfill/dist/polyfill.js',
      './index.js'],
    preprocessors: {
      './index.js': ['webpack', 'sourcemap']
    },
    concurrency: 1,
    webpack: webpackConfig,
    webpackMiddleware: {
      noInfo: true
    },
    coverageReporter: {
      dir: './coverage',
      reporters: [
        {type: 'lcov', subdir: '.'},
        {type: 'text-summary'}
      ]
    }
  })
}