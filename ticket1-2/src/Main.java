import mpi.*;

public class Main {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] array = {1, 3, 3, 4, 5}; // Вхідний масив

        int localSum = calculatePartialSum(array, rank, size);

        int globalSum = cascadeSum(localSum, rank, size);

        if (rank == 0) {
            System.out.println("Final sum: " + globalSum);
        }

        MPI.Finalize();
    }

    private static int calculatePartialSum(int[] array, int rank, int size) {
        int startIndex = rank * (array.length / size);
        int endIndex = (rank == size - 1) ? array.length : startIndex + (array.length / size);

        int sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += array[i];
        }

        return sum;
    }

    private static int cascadeSum(int localSum, int rank, int size) {
        int partner;

        for (int d = 1; d < size; d *= 2) {
            if (rank % (2 * d) == 0) {
                partner = rank + d;
                if (partner < size) {
                    int receivedSum = receiveSum(partner);
                    localSum += receivedSum;
                }
            } else {
                partner = rank - d;
                sendSum(localSum, partner);
                break;
            }
        }

        return localSum;
    }

    private static void sendSum(int sum, int destRank) {
        int[] data = {sum};
        MPI.COMM_WORLD.Send(data, 0, 1, MPI.INT, destRank, 0);
    }

    private static int receiveSum(int sourceRank) {
        int[] data = new int[1];
        Status status = MPI.COMM_WORLD.Recv(data, 0, 1, MPI.INT, sourceRank, 0);
        return data[0];
    }
}
