import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Process implements Comparable<Process> {
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

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.burstTime, other.burstTime);
    }
}

class SJFAlgorithm {

    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 7));
        processes.add(new Process(2, 2, 4));
        processes.add(new Process(3, 4, 1));
        processes.add(new Process(4, 5, 4));

        sjf(processes);
    }

    public static void sjf(List<Process> processes) {
        Collections.sort(processes, (p1, p2) -> Integer.compare(p1.arrivalTime, p2.arrivalTime));

        int currentTime = 0;
        int totalProcesses = processes.size();
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        while (!processes.isEmpty()) {
            Process shortestJob = null;

            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    if (shortestJob == null || process.burstTime < shortestJob.burstTime) {
                        shortestJob = process;
                    }
                } else {
                    break; // Stop searching as processes are sorted by arrival time
                }
            }

            if (shortestJob != null) {
                System.out.println("Executing process P" + shortestJob.id + " at time " + currentTime);

                // Calculate and update waiting time and turnaround time
                shortestJob.waitingTime = currentTime - shortestJob.arrivalTime;
                shortestJob.turnaroundTime = shortestJob.waitingTime + shortestJob.burstTime;

                // Print waiting time and turnaround time
                System.out.println("  Waiting Time: " + shortestJob.waitingTime);
                System.out.println("  Turnaround Time: " + shortestJob.turnaroundTime);

                // Update total times
                totalWaitingTime += shortestJob.waitingTime;
                totalTurnaroundTime += shortestJob.turnaroundTime;

                currentTime += shortestJob.burstTime;
                processes.remove(shortestJob);
            } else {
                currentTime++; // No eligible process, move to the next time unit
            }
        }

        // Calculate and print average waiting time and average turnaround time
        double avgWaitingTime = totalWaitingTime / totalProcesses;
        double avgTurnaroundTime = totalTurnaroundTime / totalProcesses;

        System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }
}
