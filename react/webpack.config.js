const path = require('path')
const HtmlWebPackPlugin = require('html-webpack-plugin')
const CopyPlugin = require("copy-webpack-plugin")
const TerserPlugin = require("terser-webpack-plugin")
const webpack = require('webpack');
const CompressionPlugin = require("compression-webpack-plugin")

const htmlPlugin = new HtmlWebPackPlugin({
    template: path.join(__dirname, './src/index.html'),
    filename: 'index.html'
})
const distPath = '../center/src/main/resources/static/dist'
const testHostPath = {
    target: 'YOUR_HOST_ADDR',
    secure: false,
    changeOrigin: true
}
// const testHostPath = 'http://localhost:9886'
// in dev mode the testHostPath need to add token
//      headers: {
//         'Authorization':'*******',
//     }
// About Terser https://webpack.js.org/plugins/terser-webpack-plugin/
// about ENV usage: https://webpack.js.org/guides/environment-variables/
module.exports = env => {
    console.log("env is ", env)
    return {
        mode: env.prod?"production":"development",
        output: {
            path: path.join(__dirname, distPath),
            filename: 'bundle.js'
        },
        devServer: {
            port: 9999,
            proxy: {
                '/api': testHostPath,
                '/devices': testHostPath,
                '/test': testHostPath,
                '/images': {
                    target: testHostPath,
                    pathRewrite: { '^/images': '/src/images' }
                }
            }
        },
        optimization: {
            minimize: env.prod,
            minimizer: [new TerserPlugin()],
        },
        plugins: [
            htmlPlugin,
            new CopyPlugin({
                patterns: [
                    { from: path.join(__dirname, 'images'), to: path.join(__dirname, distPath + '/images') },
                ],
            }),
            new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
            new CompressionPlugin({
                test: /\.js/,
                algorithm: 'gzip'
              })
        ],
        module: {
            rules: [
                { test: /\.js|jsx$/, use: 'babel-loader', exclude: /node_modules/ },
                {
                    test: /\.js$/,
                    enforce: 'pre',
                    use: ['source-map-loader'],
                    exclude: /node_modules/,
                },
                {
                    test: /\.css$/,
                    use: [
                        { loader: 'style-loader' },
                        { loader: 'css-loader' }
                    ]
                },
                {
                    test: /\.scss$/,
                    exclude: /node_modules/,
                    use: [
                        { loader: 'style-loader' },
                        {
                            loader: 'css-loader',
                            options: {
                                modules: { localIdentName: '[path]-[name]-[local]-[hash:6]' }
                            }
                        },
                        { loader: 'sass-loader' }
                    ]
                },
                {
                    test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
                    use: [
                        {
                            loader: "url-loader",
                            options: {
                                name: "[name].[hash:16].[ext]",
                                limit: 1024 * 8,
                                outputPath: "images",
                                publicPath: "assets/imgs",
                            },
                        }
                    ]
                },
            ]
        },
        resolve: {
            extensions: ['.js', '.jsx', '.json'],
            alias: {
                '@': path.join(__dirname, '/src')
            }
        },
        externals: {
            moment: 'moment'
        }
    }
}