import java.util.*;

class Process {
    int id;
    int arrivalTime;
    int burstTime;
    int waitingTime;
    int turnaroundTime;

    public Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }
}

public class SJFNonPreemptive {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();
System.out.println("enter number of processor");
int n=scanner.nextInt();
        // Taking input from the user for each process
        for (int i = 1; i <= n; i++) {
            System.out.println("Enter arrival time for process " + i + ": ");
            int arrivalTime = scanner.nextInt();

            System.out.println("Enter burst time for process " + i + ": ");
            int burstTime = scanner.nextInt();

            processes.add(new Process(i, arrivalTime, burstTime));
        }

        scanner.close();

        calculateSJF(processes);
    }

    public static void calculateSJF(List<Process> processes) {
        Queue<Process> arrivalQueue = new LinkedList<>(processes);
        Queue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.burstTime));

        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        while (!arrivalQueue.isEmpty() || !readyQueue.isEmpty()) {
            while (!arrivalQueue.isEmpty() && arrivalQueue.peek().arrivalTime <= currentTime) {
                readyQueue.add(arrivalQueue.poll());
            }

            if (!readyQueue.isEmpty()) {
                Process current = readyQueue.poll();
                current.waitingTime = currentTime - current.arrivalTime;
                current.turnaroundTime = current.waitingTime + current.burstTime;

                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.turnaroundTime;

                currentTime += current.burstTime;
            } else {
                currentTime = arrivalQueue.peek().arrivalTime;
            }
        }

        // Calculate averages
        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // Print results
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }
}