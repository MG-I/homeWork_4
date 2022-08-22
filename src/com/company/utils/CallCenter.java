package com.company.utils;

import com.company.data.Agent;
import com.company.data.Client;
import com.company.utils.CallCenterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CallCenter {

    private final Thread workingThread;
    private final int maxQueueSize;

    private final ConcurrentLinkedQueue<Agent> availableAgents = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Client> waitingClients = new ConcurrentLinkedQueue<>();
    private final List<Agent> agents = new ArrayList<>();
    private final List<Client> processedClients = new ArrayList<>();

    public CallCenter(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        this.workingThread = new Thread(this::startWork);

        this.workingThread.setName("Working process");
        this.workingThread.start();
        writeInConsole("Call center started to work");
    }

    public void endWork() {
        while (true) {
            if (waitingClients.size() == 0 && agents.stream().noneMatch(Agent::isBusy)) {
                this.workingThread.interrupt();
                writeInConsole("Call center ended to work");
                break;
            }
        }
    }

    private void startWork() {
        while (true) {
            if (Thread.interrupted() && waitingClients.size() == 0) {
                return;
            }
            if (waitingClients.peek() != null) {
                Agent availableAgent = findAvailableAgent();
                if (availableAgent == null) {
                    continue;
                }
                Client currentClient = waitingClients.poll();
                Thread clientThread = new Thread(() -> workWithClient(availableAgent, currentClient));
                clientThread.setName("Working with a client " + currentClient);
                clientThread.start();
            }
        }
    }

    public void registerNewAgent(Agent agent) {
        agents.add(agent);
        availableAgents.add(agent);
    }

    public void addNewClient(Client client) {
        try {
            if (waitingClients.size() >= maxQueueSize - 1) {
                throw new IllegalStateException();
            }
            writeInConsole("Client " + client + " got in line");
            waitingClients.add(client);
        } catch (IllegalStateException e) {
            writeInConsole(" Call center cannot process  client " + client + " because the queue is full");
        }
    }

    private void workWithClient(Agent agent, Client client) {
        agent.setBusy(true);
        long start = System.currentTimeMillis();
        try {
            writeInConsole("Agent " + agent + " started working with a client " + client);
            Thread.sleep(CallCenterUtils.getRandomTime());
            writeInConsole("Agent " + agent + " finished working with a client " +
                client + " in " + (System.currentTimeMillis() - start) + " milliseconds");
            synchronized (processedClients) {
                processedClients.add(client);
            }
        } catch (InterruptedException e) {
            writeInConsole("The operator's work " + agent + " with Client " + client + " has been interrupted");
        } finally {
            agent.setBusy(false);
            availableAgents.add(agent);
        }
    }

    private Agent findAvailableAgent() {
        return availableAgents.poll();
    }

    private void writeInConsole(String value) {
        System.out.println("[" + Thread.currentThread().getName() + "]" + " - " + value);
    }
}
