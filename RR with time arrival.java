import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Process {

    int id;
    int arrivalTime;
    int burstTime;
    int remainingTime;

    Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

class Mainn {
   static   int numProcesses=0;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        numProcesses  = scanner.nextInt();

        Queue<Process> processQueue = new LinkedList<>();

        // Input process details
        for (int i = 1; i <= numProcesses; i++) {
            System.out.print("Enter arrival time for process P" + i + ": ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter burst time for process P" + i + ": ");
            int burstTime = scanner.nextInt();
            processQueue.add(new Process(i, arrivalTime, burstTime));
        }

        System.out.print("Enter the time quantum: ");
        int timeQuantum = scanner.nextInt();

        // Perform Round Robin scheduling
        roundRobinScheduling(processQueue, timeQuantum);

        scanner.close();
    }

    public static void roundRobinScheduling(Queue<Process> processQueue, int timeQuantum) {
        int currentTime = 0;
        int totalWaitTime = 0;
        int totalTurnaroundTime = 0;

        while (!processQueue.isEmpty()) {
            Process currentProcess = processQueue.poll();

            if (currentProcess.arrivalTime > currentTime) {
                // Process has not arrived yet, move time forward
                currentTime = currentProcess.arrivalTime;
            }

            if (currentProcess.remainingTime <= timeQuantum) {
                // Process completes within the time quantum
                currentTime += currentProcess.remainingTime;
                totalWaitTime += currentTime - currentProcess.arrivalTime - currentProcess.burstTime;
                totalTurnaroundTime += currentTime - currentProcess.arrivalTime;

                System.out.println("P" + currentProcess.id +
                        "\tArrival Time: " + currentProcess.arrivalTime +
                        "\tBurst Time: " + currentProcess.burstTime +
                        "\tWait Time: " + (currentTime - currentProcess.arrivalTime - currentProcess.burstTime) +
                        "\tTurnaround Time: " + (currentTime - currentProcess.arrivalTime));
            } else {
                // Process needs more time
                currentTime += timeQuantum;
                currentProcess.remainingTime -= timeQuantum;
                processQueue.add(currentProcess);
            }
        }

        // Calculate averages
        double averageWaitTime = (double) totalWaitTime / numProcesses;
        double averageTurnaroundTime = (double) totalTurnaroundTime / numProcesses;

        System.out.println("\nAverage Waiting Time: " + averageWaitTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}