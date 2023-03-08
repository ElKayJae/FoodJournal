const PROXY_CONFIG = [
    {
        context: ['/**'],
        target: 'http://localhost:8080',
        secure: false
    }
]

module.exports = PROXY_CONFIG;

//ng serve --proxy-config proxy.config.js