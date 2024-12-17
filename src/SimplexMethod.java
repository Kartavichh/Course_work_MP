public class SimplexMethod {
    public static void main(String[] args) {
        // Матрица симплекс-таблицы
        // Первая строка - коэффициенты целевой функции (с минусами)
        // Остальные строки - ограничения
        double[][] tableau = {
                { -14, -18, 0, 0, 0 },  // Целевая функция: Max 14x1 + 18x2
                { 10, 8, 1, 0, 168 },   // Ограничение 1: 10x1 + 8x2 <= 168
                { 5, 10, 0, 1, 180 },   // Ограничение 2: 5x1 + 10x2 <= 180
                { 6, 12, 0, 0, 144 }    // Ограничение 3: 6x1 + 12x2 <= 144
        };

        int rows = tableau.length;
        int cols = tableau[0].length;

        // Выполнение симплекс-метода
        while (true) {
            int pivotCol = findPivotColumn(tableau);
            if (pivotCol == -1) {
                // Оптимальное решение найдено
                break;
            }

            int pivotRow = findPivotRow(tableau, pivotCol);
            if (pivotRow == -1) {
                System.out.println("Задача не имеет решения.");
                return;
            }

            performPivot(tableau, pivotRow, pivotCol);
        }

        // Вывод результата
        System.out.println("Оптимальное решение найдено:");
        for (int i = 0; i < tableau[0].length - 1; i++) {
            boolean isBasic = true;
            int basicRow = -1;
            for (int j = 1; j < tableau.length; j++) {
                if (tableau[j][i] == 1 && basicRow == -1) {
                    basicRow = j;
                } else if (tableau[j][i] != 0) {
                    isBasic = false;
                    break;
                }
            }
            if (isBasic && basicRow != -1) {
                System.out.printf("x%d = %.2f\n", i + 1, tableau[basicRow][cols - 1]);
            } else {
                System.out.printf("x%d = 0.00\n", i + 1);
            }
        }
        System.out.printf("Максимальная прибыль = %.2f\n", tableau[0][cols - 1]);
    }

    // Находим ведущий столбец (столбец с наибольшим отрицательным значением в строке цели)
    private static int findPivotColumn(double[][] tableau) {
        int pivotCol = -1;
        double min = 0;
        for (int i = 0; i < tableau[0].length - 1; i++) {
            if (tableau[0][i] < min) {
                min = tableau[0][i];
                pivotCol = i;
            }
        }
        return pivotCol;
    }

    // Находим ведущую строку (минимальное положительное отношение правой части к коэффициенту столбца)
    private static int findPivotRow(double[][] tableau, int pivotCol) {
        int pivotRow = -1;
        double minRatio = Double.MAX_VALUE;
        for (int i = 1; i < tableau.length; i++) {
            if (tableau[i][pivotCol] > 0) {
                double ratio = tableau[i][tableau[0].length - 1] / tableau[i][pivotCol];
                if (ratio < minRatio) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        return pivotRow;
    }

    // Выполняем элементарные преобразования для нового базиса
    private static void performPivot(double[][] tableau, int pivotRow, int pivotCol) {
        double pivotValue = tableau[pivotRow][pivotCol];

        // Делим строку ведущего элемента на его значение
        for (int j = 0; j < tableau[0].length; j++) {
            tableau[pivotRow][j] /= pivotValue;
        }

        // Обновляем остальные строки
        for (int i = 0; i < tableau.length; i++) {
            if (i != pivotRow) {
                double factor = tableau[i][pivotCol];
                for (int j = 0; j < tableau[0].length; j++) {
                    tableau[i][j] -= factor * tableau[pivotRow][j];
                }
            }
        }
    }
}
