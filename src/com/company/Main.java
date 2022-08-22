package com.company;

import com.company.data.Agent;
import com.company.data.CallCenter;
import com.company.data.Client;

public class Main {

    public static void main(String[] args) {
        Thread.currentThread().setName("Call center");
        int agentsCount = 3;
        int clientsCount = 8;

        CallCenter callCenter = new CallCenter(clientsCount - agentsCount);

        for (int i = 1; i <= agentsCount; i++) {
            Agent agent = new Agent();
            agent.setName(String.valueOf(i));
            callCenter.registerNewAgent(agent);
        }

        for (int i = 1; i <= clientsCount; i++) {
            Client client = new Client();
            client.setName(String.valueOf(i));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            callCenter.addNewClient(client);
        }

        callCenter.endWork();
    }
}
