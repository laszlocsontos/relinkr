const path = require('path');
const webpack = require('webpack');
const config = require(`./env/${process.env.NODE_ENV}.json`);

module.exports = {
  chainWebpack: config => {
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options['transformAssetUrls'] = {
          img: 'src',
          image: 'xlink:href',
          'b-img': 'src',
          'b-img-lazy': ['src', 'blank-src'],
          'b-card': 'img-src',
          'b-card-img': 'img-src',
          'b-card-img-lazy': ['src', 'blank-src'],
          'b-carousel-slide': 'img-src',
          'b-embed': 'src'
        };

        return options;
      });
  },
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        CONFIG: JSON.stringify(config)
      })
    ],
    resolve: {
      alias: {
        'bootstrap-components': path.resolve(__dirname, 'node_modules/bootstrap-vue/es/components')
      }
    }
  },
  devServer: {
    open: true,
    port: 9443,
    https: true,
    hot: true,
    hotOnly: true
  }
};
