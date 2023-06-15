import mpi.*;

public class Main {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] array = {1, 3, 3, 4, 5, 6}; // Вхідний масив
        int[] localSum = new int[1]; // Локальна сума

        // Обчислення локальної суми
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        localSum[0] = sum;

        // Каскадне обчислення суми
        while (size > 1) {
            if (rank % 2 == 0) {
                // Відправка локальної суми наступному процесу
                int dest = rank + 1;
                if (dest < size) ₴{
                    MPI.COMM_WORLD.Send(localSum, 0, 1, MPI.INT, dest, 0);
                }
            } else {
                // Отримання суми від попереднього процесу
                int source = rank - 1;
                MPI.COMM_WORLD.Recv(localSum, 0, 1, MPI.INT, source, 0);

                // Додавання отриманої суми до локальної суми
                localSum[0] += sum;
            }

            size /= 2; // Зменшення розміру комунікатора
        }

        // Виведення суми в останньому процесі
        if (rank == 0) {
            System.out.println("Total Sum: " + localSum[0]);
        }

        MPI.Finalize();
    }
}
