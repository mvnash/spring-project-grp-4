import fetch from 'node-fetch';

const eureka = require('eureka-js-client');

const client = new eureka.Eureka({
    instance: {
        app: 'call',
        hostName: 'localhost:9009',
        ipAddr: '127.0.0.1',
        statusPageUrl: 'http://localhost:9009/info',
        port: {
            '$': 9009,
            '@enabled': true,
        },
        vipAddress: 'call',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        }
    },
    eureka: {
        host: 'localhost',
        port: 9000,
        servicePath: '/eureka/apps/',
    }
});

client.start();

async function getServiceUrl(appName) {
    const instances = await client.getInstancesByVipAddress(appName);
    if (instances.length > 0) {
        const instance = instances[0];
        return `http://${instance.hostName}:${instance.port.$}/`;
    }
    throw new Error(`Aucune instance trouvée pour l'application ${appName}`);
}

const investorsUrl = await getServiceUrl('investor');
const walletUrl = await getServiceUrl('wallet');

async function verifyWallet() {

    const responseInvestors = await fetch(`${investorsUrl}/investor`);
    const investors = await responseInvestors.text();

    const removed = false;
    for (investor in investors) {

        const responseWallet = await fetch(`${walletUrl}/wallet/${investor.username}`);
        const wallet = await responseWallet.text();

        if (wallet.quantity < 0) {
            removed = true;
            cashToRemove = (wallet.unitvalue * Math.abs(wallet.quantity)) / 100;


            const responseWallet = await fetch(`${walletUrl}/${investor.username}`, {
                method: 'post',
                body: {
                    "ticker": "CASH",
                    "quantity": cashToRemove,
                    "unitvalue": 1
                  }
            });

        }
    }
    return removed;
}

verifyWallet()