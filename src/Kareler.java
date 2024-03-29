import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.List;

public class Kareler {
    public int size, typeCount;
    public boolean stop;
    List<List<Integer>> board;
    List<List<Integer>> possible, result, question;
    String gameStr = "";
    private static final String inpRegex = "\\[.*?\\]";

    public static class SolutionDoesNotExistException extends Exception {
        public SolutionDoesNotExistException(String msg) {
            super(msg);
        }
    }

    public static class InvalidSolutionException extends Exception {
        public InvalidSolutionException(String msg) {
            super(msg);
        }
    }

    Kareler(int size, int count) {
        this.size = size;
        this.typeCount = count == 0 ? 2 : count;
        this.stop = false;
        this.board = new ArrayList<List<Integer>>();
        this.possible = new ArrayList<List<Integer>>(this.size);
        this.result = new ArrayList<List<Integer>>(this.size);
        this.question = new ArrayList<List<Integer>>(this.size);
        initialize();
        getQ3();
        this.gameStr = generateGameString(roll(this.question));
    }

    String generateGameString(List<List<Integer>> game) {
        return game.stream()
                .map(r -> {
                    String s = r.stream()
                            .map(i -> i == -1 ? "?" : Integer.toString(i))
                            .map(j -> j + "/")
                            .reduce("", (a, b) -> a + b);
                    return s.substring(0, s.length() - 1);
                })
                .map(text -> "[" + text + "]")
                .reduce("", (el, total) -> el + total);
    }

    static class Solution {
        Integer numSolutions;
        List<String> solutions;

        Solution(Integer numSolutions$, List<String> solutions$) {
            this.numSolutions = numSolutions$;
            this.solutions = solutions$;
        }
    }

    enum Type {
        TUPLE,
        TRIPLE,
    }

    Kareler(String input) throws InvalidSolutionException, SolutionDoesNotExistException {
        List<List<Integer>> questionGrid = parseInput(input);
        this.size = questionGrid.size();
        Type type = getType(questionGrid);
        Solution s = solve(questionGrid, type);
        if (s.numSolutions == 0) {
            throw new SolutionDoesNotExistException("Solution does not exist.");
        } else if (s.numSolutions > 1) {
            throw new InvalidSolutionException("");
        }
    }

    Type getType(List<List<Integer>> q) {
        for (List<Integer> answer : q) {
            if (answer.contains(2)) {
                return Type.TRIPLE;
            }
        }
        return Type.TUPLE;
    }

    Kareler.Solution solve(List<List<Integer>> qB, Type t) {
        /* TODO: next we need to solve the game */
        // qB.stream().forEach(q -> q.stream().forEach(s -> System.out.println(s)));
        System.out.println(t);
        int numSol = solve(toGrid(qB), t);
        if (numSol == 0) {

        }
        return null;
    }

    Integer[][] toGrid(List<List<Integer>> list) {
        Integer[][] result = new Integer[list.size()][list.get(0).size()];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                result[i][j] = list.get(i).get(j);
            }
        }
        return result;
    }

    int solve(Integer[][] q, Type t) {
        this.numSolutions = 0;
        solve__(q, t);
        return numSolutions;
    }

    int getTypeCount(Type t) {
        return t == Type.TUPLE ? 2 : 3;
    }

    Boolean possible(Integer[][] q, Type t, int[] coord, int value) {
        int typesPerRow = getTypeCount(t);
        int x = coord[0];
        int y = coord[1];
        Integer temp = q[x][y];
        q[x][y] = value;
        if (!checkRow(q[x], typesPerRow)) {
            return false;
        }
        if (!checkCol(getCol(q, y), typesPerRow)) {
            return false;
        }
        q[x][y] = temp;
        return true;
    }

    private boolean checkRow(Integer[] integers, int typesPerRow) {
        int max = this.size / typesPerRow;
        int[] typeCounters = new int[typesPerRow];
        for (int i = 0; i < integers.length; i++) {
            if (integers[i] != -1 && ++(typeCounters[integers[i]]) > max) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCol(Integer[] integers, int typesPerCol) {
        return checkRow(integers, typesPerCol);
    }
    public static void main(String[] args) {
        Integer[][] integers = {
            {1,2,3},{1,2,3},{1,2,3}
        };
    }

    private Integer[] getCol(Integer[][] q, int index) {
        Integer[] result = new Integer[this.size];
        for (int i = 0; i < this.size; i++) {
            result[i] = q[i][index];
        }
        return result;
    }

    Integer numSolutions = 0;
    List<Integer[][]> solutions = new ArrayList<Integer[][]>();
    void solve__(Integer[][] q, Type t) {
        int[] firstEmpty = getFirstEmptyCells(q);
        if (firstEmpty[0] == -1) {
            solutions.add(q.clone());
            this.numSolutions++;
        }
        else if (this.numSolutions < 2) {
            int x = firstEmpty[0];
            int y = firstEmpty[1];
            for (int i = 0; i < (t == Type.TUPLE ? 2 : 3); i++) {
                if (possible(q, t, firstEmpty, i)) {
                    q[x][y] = i;
                    solve__(q, t);
                }
            }
        }
    }

    int[] getFirstEmptyCells(Integer[][] q) {
        int[] retval = new int[2];
        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < q[i].length; j++) {
                if (q[i][j] == -1) {
                    retval[0] = i;
                    retval[1] = j;
                    return retval;
                }
            }
        }
        retval[0] = -1;
        return retval;
    }

    int solveTriple(List<List<Integer>> q) {
        return -1;
    }

    List<List<Integer>> parseInput(String input) {
        List<String[]> matches = getMatches(input);
        List<List<Integer>> retval = new ArrayList<List<Integer>>();
        matches.stream().forEach(m -> {
            List<Integer> row = new ArrayList<>();
            for (String str : m) {
                row.add((str.equals("?") ? -1 : Integer.parseInt(str)));
            }
            retval.add(row);
        });
        return retval;
    }

    private List<String[]> getMatches(String input) {
        Pattern ptr = Pattern.compile(inpRegex);
        Matcher matcher = ptr.matcher(input);
        List<String[]> matches = matcher.results()
                .map((m) -> m.group())
                .map(s -> s.trim()
                        .replaceAll("\\[", "").replaceAll("\\]", ""))
                .map(s -> s.split("/"))
                .collect(Collectors.toList());
        return matches;
    }

    public List<List<Integer>> getGameBoard() {
        return this.possible;
    }

    private void initialize() {
        int[] lst;
        int[] lst0 = { 1, 1, 1, 0, 0, 0 };
        int[] lst1 = { 1, 1, 1, 1, 0, 0, 0, 0 };
        int[] lst2 = { 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 };
        int[] lst3 = { 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 };
        int[] lst21 = { 0, 0, 1, 1, 2, 2 };
        int[] lst22 = { 0, 0, 0, 1, 1, 1, 2, 2, 2 };
        int[] lst23 = { 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2 };
        if (this.typeCount == 2) {
            if (this.size == 6) {
                lst = lst0;
            } else if (this.size == 8) {
                lst = lst1;
            } else if (this.size == 10) {
                lst = lst2;
            } else {
                lst = lst3;
            }
        } else {
            if (this.size == 6) {
                lst = lst21;

            } else if (this.size == 9) {
                lst = lst22;

            } else {
                lst = lst23;
            }
        }
        findPermutations(lst, 0, lst.length);
        Collections.shuffle(this.board);
        findPossible();

    }

    private void findPossible() {
        int counter = 0, index = 0;
        boolean keepGoing = true;
        while (counter < this.size && keepGoing) {
            index = 0;
            while (index < this.board.size()) {
                this.possible.add(this.board.get(index));
                if (constrains() && colCons()) {
                    this.board.add(this.board.remove(index));
                    counter++;
                    break;
                } else if (index == this.board.size() - 1) {
                    this.possible.clear();
                    keepGoing = false;
                    break;
                }
                this.possible.remove(this.possible.size() - 1);
                index++;
            }
        }
        if (keepGoing == false) {
            this.possible.clear();
            Collections.shuffle(this.board);
            findPossible();
        }
    }

    private boolean constrains() {
        int index = 0, temp;
        int length = this.possible.size() - 1;
        if (length < 1) {
            return true;
        } else if (length > 1) {
            while (index < this.size) {
                temp = this.possible.get(length).get(index);
                if (temp == this.possible.get(length - 1).get(index)
                        && temp == this.possible.get(length - 2).get(index)) {
                    return false;
                }
                index++;
            }
        }
        return true;
    }

    private boolean colCons() {
        int countzeros = 0, counterones = 0, counttwos = 0;
        if (this.possible.size() < this.size / this.typeCount) {
            return true;
        } else {
            for (int i = 0; i < this.possible.size(); i++) {
                countzeros = 0;
                counterones = 0;
                counttwos = 0;
                for (int j = 0; j < this.possible.size(); j++) {
                    if (this.possible.get(j).get(i) == 0) {
                        countzeros++;
                    } else if (this.possible.get(j).get(i) == 1) {
                        counterones++;
                    } else if (this.possible.get(j).get(i) == 2) {
                        counttwos++;
                    }
                }
                if (countzeros > this.size / typeCount || counterones > this.size / typeCount
                        || counttwos > this.size / typeCount) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean shouldSwap(int str[], int start, int curr) {
        for (int i = start; i < curr; i++) {
            if (str[i] == str[curr]) {
                return false;
            }
        }
        return true;
    }

    private void findPermutations(int str[], int index, int n) {
        if (index >= n) {
            if (singleRowCheck(str)) {
                ArrayList<Integer> ls = new ArrayList<Integer>(str.length);
                for (Integer integer : str) {
                    ls.add(integer);
                }
                this.board.add(ls);
            }
            return;
        }
        for (int i = index; i < n; i++) {
            boolean check = shouldSwap(str, index, i);
            if (check) {
                swap(str, index, i);
                findPermutations(str, index + 1, n);
                swap(str, index, i);
            }
        }
    }

    private void swap(int[] str, int i, int j) {
        int c = str[i];
        str[i] = str[j];
        str[j] = c;
    }

    private boolean singleRowCheck(int[] row) {
        int counter = 0, type;
        while (counter < this.size) {
            type = row[counter];
            if (counter == 0) {
                if (type == row[counter + 1] && type == row[counter + 2]) {
                    return false;
                }
            } else if (counter == 1) {
                if (type == row[counter - 1] && type == row[counter + 1]) {
                    return false;
                } else if (type == row[counter + 1] && type == row[counter + 2]) {
                    return false;
                }
            } else if (counter > 1 && counter < this.size - 2) {
                if (type == row[counter - 1] && type == row[counter - 2]) {
                    return false;
                }
                if (type == row[counter - 1] && type == row[counter + 1]) {
                    return false;
                }
                if (type == row[counter + 1] && type == row[counter + 2]) {
                    return false;
                }
            } else if (counter == this.size - 2) {
                if (type == row[counter - 1] && type == row[counter + 1]) {
                    return false;
                }
                if (type == row[counter - 1] && type == row[counter - 2]) {
                    return false;
                }
            } else if (counter == this.size - 1) {
                if (type == row[counter - 1] && type == row[counter - 2]) {
                    return false;
                }
            }
            counter++;
        }
        return true;
    }

    public boolean isSolvable2() {
        int resultCounter = 0, prev;
        while (true) {
            prev = count(this.result);
            // System.out.println("Starts before partial");

            /*
             * System.out.println("After partial before cols");
             * for (ArrayList<Integer> arrayList : this.result) {
             * System.out.println(arrayList);
             * }
             */
            solveRows2();
            /*
             * System.out.println("After cols before rows");
             * for (ArrayList<Integer> arrayList : result) {
             * System.out.println(arrayList);
             * }
             */
            solveCols2();
            /*
             * System.out.println("Ended after rows");
             * for (ArrayList<Integer> arrayList : result) {
             * System.out.println(arrayList);
             * }
             */
            solvePartial(this.result);
            resultCounter = count(this.result);
            if (prev == resultCounter) {
                // System.out.println("Returnin False");
                return false;
            }
            if (isSolved(this.result)) {
                // System.out.println("Returning True");
                return true;
            }
        }

    }

    private void solvePartial(List<List<Integer>> list) {
        int str;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (list.get(i).get(j) == -1) {
                    if (j > 1) {
                        // sol
                        if (list.get(i).get(j - 1) == list.get(i).get(j - 2) && list.get(i).get(j - 1) != -1) {
                            str = list.get(i).get(j - 1) == 0 ? 1 : 0;
                            list.get(i).set(j, str);
                            continue;
                        }
                    }
                    if (j < size - 2) {
                        // sag
                        if (list.get(i).get(j + 1) == list.get(i).get(j + 2) && list.get(i).get(j + 1) != -1) {
                            str = list.get(i).get(j + 1) == 0 ? 1 : 0;
                            list.get(i).set(j, str);
                            continue;
                        }
                    }
                    if (j > 0 && j < size - 1) {
                        // sagsol
                        if (list.get(i).get(j - 1) == list.get(i).get(j + 1) && list.get(i).get(j + 1) != -1) {
                            str = list.get(i).get(j + 1) == 0 ? 1 : 0;
                            list.get(i).set(j, str);
                            continue;
                        }
                    }
                    if (i > 1) {
                        // ust
                        if (list.get(i - 1).get(j) == list.get(i - 2).get(j) && list.get(i - 1).get(j) != -1) {
                            str = list.get(i - 1).get(j) == 0 ? 1 : 0;
                            list.get(i).set(j, str);
                            continue;
                        }
                    }
                    if (i < size - 2) {
                        // alt
                        if (list.get(i + 1).get(j) == list.get(i + 2).get(j) && list.get(i + 1).get(j) != -1) {
                            str = list.get(i + 1).get(j) == 0 ? 1 : 0;
                            list.get(i).set(j, str);
                            continue;
                        }
                    }
                    if (i > 0 && i < size - 1) {
                        // altust
                        if (list.get(i + 1).get(j) == list.get(i - 1).get(j) && list.get(i - 1).get(j) != -1) {
                            str = list.get(i - 1).get(j) == 0 ? 1 : 0;
                            list.get(i).set(j, str);
                            continue;
                        }
                    }
                }
            }
        }

    }

    private void solveCols2() {
        int countzeros = 0, counterones = 0;
        int typ = this.size / this.typeCount;
        for (int i = 0; i < this.size; i++) {
            countzeros = 0;
            counterones = 0;
            for (int j = 0; j < this.size; j++) {
                if (this.result.get(j).get(i) == 0) {
                    countzeros++;
                } else if (this.result.get(j).get(i) == 1) {
                    counterones++;
                }
            }
            if (counterones == typ || countzeros == typ) {
                for (int j = 0; j < this.size; j++) {
                    this.result.get(j).set(i, this.possible.get(j).get(i));
                }
            }
        }
    }

    private void solveRows2() {
        int countzeros = 0, counterones = 0;
        int typ = this.size / this.typeCount;
        for (int i = 0; i < this.size; i++) {
            countzeros = 0;
            counterones = 0;
            for (int j = 0; j < this.size; j++) {
                if (this.result.get(i).get(j) == 0) {
                    countzeros++;
                } else if (this.result.get(i).get(j) == 1) {
                    counterones++;
                }
            }
            if (counterones == typ || countzeros == typ) {
                for (int j = 0; j < this.size; j++) {
                    this.result.get(i).set(j, this.possible.get(i).get(j));
                }
            }
        }
    }

    private void getQ3() {
        int rand1, rand2, keeper = -1, trys = 0, total;
        boolean solved = false;
        Random rand = new Random();
        total = this.size * this.size / 4;
        if (this.typeCount == 3) {
            switch (this.size) {
                case 6:
                    total = this.size * this.size / 3;
                    break;
                case 9:
                    total = 30;
                    break;
                case 12:
                    total = 63;
                    break;
                default:
                    break;
            }
        }
        for (List<Integer> arrayList : this.possible) {
            this.result.add(new ArrayList<Integer>(arrayList));
            this.question.add(new ArrayList<Integer>(arrayList));
        }
        while (true) {
            rand1 = rand.nextInt(this.size);
            rand2 = rand.nextInt(this.size);
            if (this.result.get(rand1).get(rand2) != -1) {
                keeper = this.result.get(rand1).set(rand2, -1);
                this.question.get(rand1).set(rand2, -1);
            } else {
                continue;
            }
            trys++;
            solved = this.typeCount == 2 ? isSolvable2() : solveResult();
            if (solved) {
                trys = 0;
                if (count(this.question) <= total) {
                    break;
                }
            } else {
                this.question.get(rand1).set(rand2, keeper);
                if (trys >= count(question)) {
                    trys = 0;
                    this.question.clear();
                    for (List<Integer> arrayList : this.possible) {
                        this.question.add(new ArrayList<Integer>(arrayList));
                    }
                }
            }
            this.result.clear();
            for (List<Integer> arrayList : this.question) {
                this.result.add(new ArrayList<Integer>(arrayList));
            }
        }

    }

    private boolean solveResult() {
        int counter, prevcount;
        while (true) {
            prevcount = count(this.result);
            checkConstrains();
            counter = count(this.result);
            if (this.isSolved(this.result)) {
                break;
            }
            if (counter == prevcount) {
                System.out.println("Returning False and " + count(this.question));
                return false;
            }
        }
        return true;
    }

    private int count(List<List<Integer>> list) {
        int counter = 0;
        for (List<Integer> arrayList : list) {
            for (Integer integer : arrayList) {
                if (integer != -1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private void checkConstrains() {
        this.solveRows();
        this.solveCols();
        this.constrainsOne();
    }

    private void constrainsOne() {
        int rcounttwos = 0, rcountones = 0, rcountzeros = 0, counttwos = 0, countones = 0, countzeros = 0, cell, temp,
                temp2;
        int difference = this.size / this.typeCount;
        for (int i = 0; i < this.size; i++) {
            counttwos = 0;
            countones = 0;
            countzeros = 0;
            for (int j = 0; j < this.size; j++) {
                cell = this.result.get(i).get(j);
                if (cell == -1) {
                    rcounttwos = 0;
                    rcountones = 0;
                    rcountzeros = 0;
                    counttwos = 0;
                    countones = 0;
                    countzeros = 0;
                    for (int j2 = 0; j2 < this.size; j2++) {
                        temp = this.result.get(i).get(j2);
                        temp2 = this.result.get(j2).get(j);
                        if (temp == 0) {
                            rcountzeros++;
                        } else if (temp == 1) {
                            rcountones++;
                        } else if (temp == 2) {
                            rcounttwos++;
                        }
                        if (temp2 == 0) {
                            countzeros++;
                        } else if (temp2 == 1) {
                            countones++;
                        } else if (temp2 == 2) {
                            counttwos++;
                        }
                    }
                    // 0
                    if ((rcountones == difference && counttwos == difference)
                            || (countones == difference && rcounttwos == difference)) {
                        this.result.get(i).set(j, 0);
                    }
                    // 1
                    else if ((rcountzeros == difference && counttwos == difference)
                            || (countzeros == difference && rcounttwos == difference)) {
                        this.result.get(i).set(j, 1);
                    }
                    // 2
                    else if ((rcountzeros == difference && countones == difference)
                            || (countzeros == difference && rcountones == difference)) {
                        this.result.get(i).set(j, 2);
                    }

                }
            }
        }
    }

    private void solveRows() {
        int typ = this.size / this.typeCount;
        int countzeros = 0, counterones = 0, countertwos = 0;
        for (int i = 0; i < this.size; i++) {
            countzeros = 0;
            counterones = 0;
            countertwos = 0;
            for (int j = 0; j < this.size; j++) {
                if (this.result.get(i).get(j) == 0) {
                    countzeros++;
                } else if (this.result.get(i).get(j) == 1) {
                    counterones++;
                } else if (this.result.get(i).get(j) == 2) {
                    countertwos++;
                }
            }
            if ((counterones == typ && countzeros == typ) || (countertwos == typ && counterones == typ)
                    || (countertwos == typ && countzeros == typ)) {
                for (int j = 0; j < this.size; j++) {
                    this.result.get(i).set(j, this.possible.get(i).get(j));
                }
            }
        }
    }

    private void solveCols() {
        int typ = this.size / this.typeCount;
        int countzeros = 0, counterones = 0, countertwos = 0;
        for (int i = 0; i < this.size; i++) {
            countzeros = 0;
            counterones = 0;
            countertwos = 0;
            for (int j = 0; j < this.size; j++) {
                if (this.result.get(j).get(i) == 0) {
                    countzeros++;
                } else if (this.result.get(j).get(i) == 1) {
                    counterones++;
                } else if (this.result.get(j).get(i) == 2) {
                    countertwos++;
                }
            }
            if ((counterones == typ && countzeros == typ) || (countertwos == typ && counterones == typ)
                    || (countertwos == typ && countzeros == typ)) {
                for (int j = 0; j < this.size; j++) {
                    this.result.get(j).set(i, this.possible.get(j).get(i));
                }
            }
        }
    }

    private boolean isSolved(List<List<Integer>> list) {
        for (List<Integer> arrayList : list) {
            for (Integer arrayList2 : arrayList) {
                if (arrayList2 == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    <T> List<List<T>> roll(List<List<T>> list) {
        List<List<T>> result = new ArrayList<List<T>>();
        for (int i = 0; i < list.size(); i++) {
            List<T> row = new ArrayList<T>();
            for (int j = 0; j < list.get(i).size(); j++) {
                row.add(list.get(j).get(i));
            }
            result.add(row);
        }
        return result;
    }
}
