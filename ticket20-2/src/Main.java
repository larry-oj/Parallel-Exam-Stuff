import mpi.*;

public class Main {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] C = null;
        int[] localC = new int[1];

        if (rank == 0) {
            // Initialize array C in the master process
            C = new int[]{5, 3, 8, 2, 9, 1, 4, 6, 7};
            // Scatter fragments of array C to worker processes
            MPI.COMM_WORLD.Scatter(C, 0, 1, MPI.INT, localC, 0, 1, MPI.INT, 0);
        }

        // Find the minimum value in worker processes
        int[] localMin = {findMin(localC)};

        // Reduce the found minimum values to the master process
        int[] globalMin = new int[1];
        MPI.COMM_WORLD.Reduce(localMin, 0, globalMin, 0, 1, MPI.INT, MPI.MIN, 0);

        // Print the global minimum value in the master process
        if (rank == 0) {
            System.out.println("Global Min: " + globalMin[0]);
        }

        MPI.Finalize();
    }

    private static int findMin(int[] array) {
        int min = Integer.MAX_VALUE;
        for (int value : array) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }
}
