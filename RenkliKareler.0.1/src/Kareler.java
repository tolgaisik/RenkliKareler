import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
public class Kareler {
    public int size,typeCount;
    ArrayList<ArrayList<Integer>> board;
    ArrayList<ArrayList<Integer>> possible;
    Kareler(int size, int count) {
        System.out.println("Start...");
        this.size = size;
        this.typeCount = count == 0 ? 2 : count;
        this.board = new ArrayList<ArrayList<Integer>>();
        this.possible = new ArrayList<ArrayList<Integer>>(); 
        this.initialize();
        for (ArrayList<Integer> arrayList : this.possible) {
            System.out.println(arrayList);
        }
    }
    public ArrayList<ArrayList<Integer>> getGameBoard() {
        return this.possible;
    }
    private void initialize() {
        int[] lst;
        int[] lst0 = {1,1,1,0,0,0};
        int[] lst1 = {1,1,1,1,0,0,0,0};
        int[] lst2 = {1,1,1,1,1,0,0,0,0,0};
        int[] lst3 = {1,1,1,1,1,1,0,0,0,0,0,0};
        int[] lst21 = {0,0,1,1,2,2};
        int[] lst22 = {0,0,0,1,1,1,2,2,2};
        int[] lst23 = {0,0,0,0,1,1,1,1,2,2,2,2};
        if(this.typeCount == 2) {
            if(this.size == 6) {
                lst = lst0;
            }
            else if(this.size == 8) {
                lst = lst1;
            }
            else if(this.size == 10) {
                lst = lst2;
            }
            else {
                lst = lst3;
            }
        }
        else {
            if(this.size == 6) {
                lst = lst21;

            }
            else if(this.size == 9) {
                lst = lst22;
            }
            else {
                lst = lst23;
            }
        }
        this.findPermutations(lst,0,lst.length);
        Collections.shuffle(this.board);
        findPossible();
        
    }
    private void findPossible() {
        int counter = 0,index = 0;
        boolean keepGoing = true;
        while(counter < this.size && keepGoing) {
            index = 0;
            while(index < this.board.size()) {
                this.possible.add(this.board.get(index));
                if(constrains() && colCons()) {
                    this.board.add(this.board.remove(index));
                    counter++;
                    break;
                }
                else if (index == this.board.size()-1) {
                    this.possible.clear();
                    keepGoing = false;
                    break;
                }
                this.possible.remove(this.possible.size()-1);
                index ++;
            }
        }
        if(keepGoing == false) {
            this.possible.clear();
            Collections.shuffle(this.board);
            this.findPossible();
        }
    }
    private boolean constrains() {
        int index = 0,temp;
        int length = this.possible.size() - 1;
        if(length < 1) {
            return true;
        }
        else if(length > 1){
            while(index < this.size) {
                temp = this.possible.get(length).get(index);
                if(temp == this.possible.get(length-1).get(index) && temp == this.possible.get(length-2).get(index)) {
                    return false;
                } 
                index++;
            }
        }
        return true;
    }
    private boolean colCons() {
        int countzeros=0,counterones=0,counttwos=0;
        if(this.possible.size() < this.size/this.typeCount) {
            return true;
        }
        else {
            for (int i = 0; i < this.possible.size(); i++) {
                countzeros = 0;counterones = 0;counttwos = 0;
                for(int j = 0; j < this.possible.size(); j++) {
                    if(this.possible.get(j).get(i) == 0) {
                       countzeros++; 
                    }
                    else if (this.possible.get(j).get(i) == 1) {
                        counterones++;
                    }
                    else if(this.possible.get(j).get(i) == 2) {
                        counttwos ++;
                    }
                }
                if(countzeros > this.size/typeCount || counterones > this.size/typeCount || counttwos > this.size/typeCount) {
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
            if(this.singleRowCheck(str)) {
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
        int counter = 0,type;
        while(counter < this.size) {
            type = row[counter];
            if(counter == 0) {
                if(type == row[counter+1] && type == row[counter+2]) {
                    return false;
                }
            }
            else if(counter == 1) {
                if(type == row[counter-1] && type == row[counter+1]) {
                    return false;
                }
                else if(type == row[counter+1] && type == row[counter+2]) {
                    return false;
                }
            }
            else if(counter > 1 && counter < this.size - 2) {
                if(type == row[counter-1] && type == row[counter-2]) {
                    return false;
                }
                if(type == row[counter-1] && type == row[counter+1]) {
                    return false;
                }
                if(type == row[counter+1] && type == row[counter+2]) {
                    return false;
                }   
            }
            else if(counter == this.size - 2) {
                if(type == row[counter-1] && type == row[counter+1]) {
                    return false;
                }
                if(type == row[counter-1] && type == row[counter-2]) {
                    return false;
                }
            }
            else if(counter == this.size - 1) {
                if(type == row[counter-1] && type == row[counter-2]) {
                    return false;
                }
            }
            counter++;
        }
        return true;
    }

 
}
