import java.util.*;

class Process {
    int number;
    int arrivalTime;
    int cpuTime;
    int priority;
}

public class Main {
    static Comparator<Process> comparator = new Comparator<Process>() {
        @Override
        public int compare(Process p1, Process p2) {
            if (p1.priority != p2.priority) {
                return Integer.compare(p1.priority, p2.priority);
            } else {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        }
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int size = scanner.nextInt();
        Queue<Process> chain = new LinkedList<>();
        Map<Integer, Integer> check = new HashMap<>();
        Map<Integer, Process> check2 = new HashMap<>();

        for (int i = 0; i < size; ++i) {
            Process p = new Process();
            System.out.print("Enter process Number: ");
            p.number = scanner.nextInt();
            System.out.print("Enter process arrival time: ");
            p.arrivalTime = scanner.nextInt();
            System.out.print("Enter process cpu time: ");
            p.cpuTime = scanner.nextInt();
            System.out.print("Enter process priority: ");
            p.priority = scanner.nextInt();
            chain.add(p);
            check.put(p.arrivalTime, 1);
            check2.put(p.arrivalTime, p);
        }

        int timeSoFar = 0;
        int processCompleted = 0;
        LinkedList<Process> list = new LinkedList<>();
        int[] waitingTime = new int[size];
        int[] turnaroundTime = new int[size];
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        Queue<Process> sortedProcess = new LinkedList<>();

        while (processCompleted < size) {
            if (list.isEmpty()) {
                if (check.containsKey(timeSoFar)) {
                    Process p = check2.get(timeSoFar);
                    sortedProcess.add(p);
                    waitingTime[p.number - 1] = timeSoFar - p.arrivalTime;
                    int temp = timeSoFar;
                    for (int i = timeSoFar; i < p.cpuTime + temp; ++i) {
                        if (check.containsKey(i) && p.number != check2.get(i).number) {
                            list.add(check2.get(i));
                        }
                        timeSoFar++;
                    }
                    turnaroundTime[p.number - 1] = timeSoFar - p.arrivalTime;
                    processCompleted++;
                    continue;
                }
            } else {
                list.sort(comparator);
                Process p = list.getFirst();
                sortedProcess.add(p);
                list.removeFirst();
                waitingTime[p.number - 1] = timeSoFar - p.arrivalTime;
                int temp = timeSoFar;
                for (int i = timeSoFar; i < p.cpuTime + temp; ++i) {
                    if (check.containsKey(i)) {
                        list.add(check2.get(i));
                    }
                    timeSoFar++;
                }
                turnaroundTime[p.number - 1] = timeSoFar - p.arrivalTime;
                processCompleted++;
                continue;
            }
            timeSoFar++;
        }

        System.out.print("The Order Of Processes: ");
        System.out.println();
        while (!sortedProcess.isEmpty()) {
            System.out.print("p" + sortedProcess.poll().number + "   ");
        }
        System.out.println();

        for (int i = 1; i <= size; ++i) {
            System.out.println("Process number " + i + " has waiting time " + waitingTime[i - 1] +
                    " and Turnaround time " + turnaroundTime[i - 1]);
            totalWaitingTime += waitingTime[i - 1];
            totalTurnaroundTime += turnaroundTime[i - 1];
        }

        double avgWaitingTime = totalWaitingTime / size;
        double avgTurnaroundTime = totalTurnaroundTime / size;
        System.out.println("The Average waiting time " + avgWaitingTime + " ms");
        System.out.println("The Average Turnaround time " + avgTurnaroundTime + " ms");

        scanner.close();
    }
}
