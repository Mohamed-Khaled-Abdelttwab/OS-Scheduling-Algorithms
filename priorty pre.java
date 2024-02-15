import java.util.*;

class Process {
    int number;
    int arrivalTime;
    int cpuTime;
    int priority;

    Process(int number, int arrivalTime, int cpuTime, int priority) {
        this.number = number;
        this.arrivalTime = arrivalTime;
        this.cpuTime = cpuTime;
        this.priority = priority;
    }

    Process() {
    }
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
        Map<Integer, Process> processNumber = new HashMap<>();

        for (int i = 0; i < size; ++i) {
            int number, arrivalTime, cpuTime, priority;
            System.out.print("Enter process Number: ");
            number = scanner.nextInt();
            System.out.print("Enter process arrival time: ");
            arrivalTime = scanner.nextInt();
            System.out.print("Enter process cpu time: ");
            cpuTime = scanner.nextInt();
            System.out.print("Enter process priority: ");
            priority = scanner.nextInt();

            Process p = new Process(number, arrivalTime, cpuTime, priority);
            chain.add(p);
            check.put(arrivalTime, 1);
            check2.put(arrivalTime, p);
            processNumber.put(number, p);
        }

        int timeSoFar = 0;
        int processCompleted = 0;
        Deque<Process> remaining = new LinkedList<>();
        List<Process> vList = new ArrayList<>();
        Map<Integer, List<Integer>> scheduling = new HashMap<>();

        while (processCompleted < size) {
            if (processCompleted + remaining.size() != size) {
                if (check.getOrDefault(timeSoFar, 0) != 0) {
                    Process p = check2.get(timeSoFar);

                    if (vList.isEmpty()) {
                        vList.add(p);
                        scheduling.computeIfAbsent(p.number, k -> new ArrayList<>()).add(timeSoFar);
                    } else {
                        if (comparator.compare(vList.get(vList.size() - 1), p) > 0) {
                            scheduling.get(vList.get(vList.size() - 1).number).add(timeSoFar);
                            scheduling.computeIfAbsent(p.number, k -> new ArrayList<>()).add(timeSoFar);
                            remaining.add(vList.get(vList.size() - 1));
                            vList.add(p);
                        } else if (vList.get(vList.size() - 1).priority <= 0) {
                            remaining.add(p);
                            List<Process> remainingList = new ArrayList<>(remaining);
                            remainingList.sort(comparator);
                            scheduling.computeIfAbsent(remainingList.get(0).number, k -> new ArrayList<>()).add(timeSoFar);
                            vList.add(remainingList.get(0));
                            remaining.remove(remainingList.get(0));
                        } else {
                            remaining.add(p);
                        }
                    }
                }
            } else {
                timeSoFar--;
                List<Process> remainingList = new ArrayList<>(remaining);
                remainingList.sort(comparator);
                for (Process process : remainingList) {
                    vList.add(process);
                    scheduling.computeIfAbsent(process.number, k -> new ArrayList<>()).add(timeSoFar);
                    processCompleted++;
                    timeSoFar += process.cpuTime;
                    scheduling.computeIfAbsent(process.number, k -> new ArrayList<>()).add(timeSoFar);
                }
                continue;
            }

            if (!vList.isEmpty()) {
                if (vList.get(vList.size() - 1).cpuTime > 0) {
                    vList.get(vList.size() - 1).cpuTime -= 1;
                } else {
                    if (vList.get(vList.size() - 1).cpuTime == 0) {
                        processCompleted++;
                        scheduling.computeIfAbsent(vList.get(vList.size() - 1).number, k -> new ArrayList<>()).add(timeSoFar);
                    }
                }
            }
            timeSoFar++;
        }

        System.out.print("The Order Of Processes: ");
        System.out.println();
        System.out.print("[ ");
        for (Process process : vList) {
            System.out.print("P" + process.number + " ");
        }
        System.out.println("]");
        System.out.println();

        for (int i = 1; i <= size; ++i) {
            System.out.print("P" + i + " times");
            System.out.println();
            for (int j = 0; j < scheduling.get(i).size(); ++j) {
                System.out.print(scheduling.get(i).get(j) + " ");
            }
            System.out.println();
        }

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (int i = 1; i <= size; ++i) {
            double waitingTimeForProcess = 0;
            double turnaroundTimeForProcess = 0;

            for (int j = 0; j < scheduling.get(i).size(); j += 2) {
                if (j == 0) {
                    waitingTimeForProcess += (scheduling.get(i).get(j) - processNumber.get(i).arrivalTime);
                } else {
                    waitingTimeForProcess += (scheduling.get(i).get(j) - scheduling.get(i).get(j - 1));
                }
            }

            turnaroundTimeForProcess = scheduling.get(i).get(scheduling.get(i).size() - 1) - processNumber.get(i).arrivalTime;

            System.out.println("Process P" + i + " Has waiting time " + waitingTimeForProcess +
                    " and turnaround time " + turnaroundTimeForProcess);
            totalWaitingTime += waitingTimeForProcess;
            totalTurnaroundTime += turnaroundTimeForProcess;
        }

        double averageWaitingTime = totalWaitingTime / size;
        double averageTurnaroundTime = totalTurnaroundTime / size;
        System.out.println("The Average Waiting time = " + averageWaitingTime + " ms");
        System.out.println("The Average Turnaround time = " + averageTurnaroundTime + " ms");
    }
}
