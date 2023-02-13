import java.util.*;

// A class to store a Job
class Job
{
    int start, finish, profit;
    Job(int start, int finish, int profit)
    {
        this.start = start;
        this.finish = finish;
        this.profit = profit;
    }
    @Override
    public String toString() {
        return "(" + this.start + ", " + this.finish + ", " + this.profit + ") ";
    }
}

class Main
{
    //Actual array in which 1st index is going to store number of works for others and
    //second index stores number of earning done by others as said in statement
    //Declaring outside because we need it at both the methods

    static int[] answer = new int[2];
    static int totalEarnings;
    // Function to perform a binary search on the given jobs, which are sorted
    // by finish time. The function returns the index of the last job, which
    // doesn't conflict with the given job, i.e., whose finish time is
    // less than or equal to the given job's start time.
    public static int findLastNonConflictingJob(List<Job> jobs, int n)
    {
        // search space
        int low = 0;
        int high = n;

        // iterate till the search space is exhausted
        while (low <= high)
        {
            int mid = (low + high) / 2;
            if (jobs.get(mid).finish <= jobs.get(n).start)
            {
                if (jobs.get(mid + 1).finish <= jobs.get(n).start) {
                    low = mid + 1;
                }
                else {
                    return mid;
                }
            }
            else {
                high = mid - 1;
            }
        }

        // return the negative index if no non-conflicting job is found
        return -1;
    }

    // Function to print the non-overlapping jobs involved in maximum profit
    // using dynamic programming
    public static void findMaxProfitJobs(List<Job> jobs, int numberOfJobs)
    {
        // sort jobs in increasing order of their finish times
        Collections.sort(jobs, Comparator.comparingInt(x -> x.finish));

        // get the number of jobs
        int n = jobs.size();

        // base case
        if (n == 0) {
            return;
        }

        // `maxProfit[i]` stores the maximum profit possible for the first `i` jobs,
        // and `tasks[i]` stores the index of jobs involved in the maximum profit
        int[] maxProfit = new int[n];

        List<List<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tasks.add(new ArrayList<>());
        }

        // initialize `maxProfit[0]` and `tasks[0]` with the first job
        maxProfit[0] = jobs.get(0).profit;
        tasks.get(0).add(0);

        // fill `tasks[]` and `maxProfit[]` in a bottom-up manner
        for (int i = 1; i < n; i++)
        {
            // find the index of the last non-conflicting job with the current job
            int index = findLastNonConflictingJob(jobs, i);

            // include the current job with its non-conflicting jobs
            int currentProfit = jobs.get(i).profit;
            if (index != -1) {
                currentProfit += maxProfit[index];
            }

            // if including the current job leads to the maximum profit so far
            if (maxProfit[i-1] < currentProfit)
            {
                maxProfit[i] = currentProfit;

                if (index != -1) {
                    tasks.set(i, new ArrayList<>(tasks.get(index)));
                }
                tasks.get(i).add(i);
            }

            // excluding the current job leads to the maximum profit so far
            else {
                tasks.set(i, new ArrayList<>(tasks.get(i-1)));
                maxProfit[i] = maxProfit[i-1];
            }
        }
        answer[0] = (numberOfJobs - tasks.get(n-1).size());
        answer[1] += totalEarnings-maxProfit[n-1];
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        totalEarnings = 0;

        System.out.println("Enter the number of Jobs : ");
        int numberOfJobs = sc.nextInt();

        System.out.println("Enter job start time, end time, and earnings");
        Job[]job = new Job[numberOfJobs];
        for (int i=0; i<3; i++){
            int start = sc.nextInt();
            int end = sc.nextInt();
            int profit = sc.nextInt();
            totalEarnings += profit;
            job[i] = new Job(start,end,profit);
        }

        List<Job> jobs = Arrays.asList(job);
        findMaxProfitJobs(jobs,numberOfJobs);

        System.out.println("The number of tasks and earnings available for others Tasks : " + answer[0]);
        System.out.println("Earnings: " + answer[1]);
    }
}
