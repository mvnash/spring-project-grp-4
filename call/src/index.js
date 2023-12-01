import fetch from "node-fetch";

const eureka = require("eureka-js-client");

const client = new eureka.Eureka({
  instance: {
    app: "call",
    hostName: "localhost:9009",
    ipAddr: "127.0.0.1",
    statusPageUrl: "http://localhost:9009/info",
    port: {
      $: 9009,
      "@enabled": true,
    },
    vipAddress: "call",
    dataCenterInfo: {
      "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
      name: "MyOwn",
    },
  },
  eureka: {
    host: "localhost",
    port: 9000,
    servicePath: "/eureka/apps/",
  },
});

client.start(async () => {
  const investorsUrl = await getServiceUrl("investor");
  const walletUrl = await getServiceUrl("wallet");

  const responseInvestors = await fetch(`${investorsUrl}/investor`);
  const investors = await responseInvestors.text();

  for (investor in investors) {
    const responseWallet = await fetch(
      `${walletUrl}/wallet/${investor.username}`
    );
    const wallets = await responseWallet.text();

    for (wallet in wallets) {
      if (wallet.quantity < 0) {
        cashToRemove = (wallet.unitvalue * Math.abs(wallet.quantity)) / 100;

        await fetch(
          `${walletUrl}/${investor.username}`,
          {
            method: "post",
            body: {
              ticker: "CASH",
              quantity: cashToRemove,
              unitvalue: 1,
            },
          }
        );
      }
    }
  }
  
  client.stop()
});

async function getServiceUrl(appName) {
  const instances = await client.getInstancesByVipAddress(appName);
  if (instances.length > 0) {
    const instance = instances[0];
    return `http://${instance.hostName}:${instance.port.$}/`;
  }
  throw new Error(`Aucune instance trouvée pour l'application ${appName}`);
}
