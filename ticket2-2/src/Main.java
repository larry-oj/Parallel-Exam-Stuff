import mpi.*;

public class Main {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        // Initialize the array
        int[] globalArray = null;
        int[] localArray = new int[1];

        if (rank == 0) {
            globalArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            // Scatter the array to all processes
            MPI.COMM_WORLD.Bcast(globalArray, 0, globalArray.length, MPI.INT, 0);
        }

        // Compute the local sum
        int localSum = 0;
        for (int i = 0; i < localArray.length; i++) {
            localSum += localArray[i];
        }

        // Cascade sum
        int step = 1;
        while (step < size) {
            if (rank % (2 * step) == 0) {
                if (rank + step < size) {
                    int[] receivedSum = new int[1];
                    MPI.COMM_WORLD.Recv(receivedSum, 0, 1, MPI.INT, rank + step, 0);
                    localSum += receivedSum[0];
                }
            } else {
                int dest = rank - step;
                MPI.COMM_WORLD.Send(new int[]{localSum}, 0, 1, MPI.INT, dest, 0);
                break;
            }
            step *= 2;
        }

        // Reduce the global sum on process 0
        int[] globalSum = new int[1];
        int[] tempArray = new int[]{localSum}; // Create a temporary array
        MPI.COMM_WORLD.Reduce(tempArray, 0, globalSum, 0, 1, MPI.INT, MPI.SUM, 0);

        // Print the result
        if (rank == 0) {
            System.out.println("Total Sum: " + globalSum[0]);
        }

        MPI.Finalize();
    }
}
