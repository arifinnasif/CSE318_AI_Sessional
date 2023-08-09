#include <bits/stdc++.h>

using namespace std;

enum Move{BLANK_UP, BLANK_DOWN, BLANK_RIGHT, BLANK_LEFT};

class State {
    vector<int> permution;
    int n;
    int blank_idx;

    int heu_ham;
    int heu_man;
    int known_cost;
    Move move;

    State *parent;


    int calc_ham() {
        int temp = 0;
        for(int i = 0; i < n*n; i++) {
            if(i == blank_idx) continue;
            if(permution[i] != i+1) temp++;
        }
        return temp;
    }

    int calc_man() {
        int temp = 0;
        for(int i = 0; i < n*n; i++) {
            if(i == blank_idx) continue;
            int target_col = (permution[i]-1) % n;
            int target_row = (permution[i]-1) / n;

            int col = (i) % n;
            int row = (i) / n;

            temp += abs(target_col - col) + abs(target_row - row);
        }
        return temp;
    }

    // int calc_inv() {
    //     int temp = 0;
    //     for(int i = 0; i < n - 1; i++) {
    //         if(i == blank_idx) continue;
    //         for(int j = i + 1; j < n; j++) {
    //             if(j == blank_idx) continue;
    //             if(permution[i] > permution[j]) temp++;
    //         }
    //     }
    // }


    public:
    State(int n, int * arr) {
        if(n<2) exit(3);
        this->n = n;
        this->parent = NULL;


        for(int i = 0; i < n*n; i++) {
            this->permution.push_back(arr[i]);
            if(arr[i] == 0) {
                this->blank_idx = i;
            }
        }

        this->heu_ham = calc_ham();
        this->heu_man = calc_man();
        this->known_cost = 0;
        // inv = calc_inv();
        
    };

    State(State *arg_parent, Move arg_move) {
        if(arg_parent->n < 2) exit(3);
        this->parent = arg_parent;
        
        this->n = arg_parent->n;

        for(int i = 0; i < n*n; i++) {
            this->permution.push_back(arg_parent->permution[i]);
        }
        this->blank_idx = arg_parent->blank_idx;

        if(arg_move == BLANK_UP) {
            if(!arg_parent->is_blank_up_valid()) exit(2);
            permution[blank_idx] = permution[blank_idx - n];
            blank_idx = blank_idx - n;
            permution[blank_idx] = 0;

        } else if(arg_move == BLANK_DOWN) {
            if(!arg_parent->is_blank_down_valid()) exit(2);
            permution[blank_idx] = permution[blank_idx + n];
            blank_idx = blank_idx + n;
            permution[blank_idx] = 0;
            
        } else if(arg_move == BLANK_LEFT) {
            if(!arg_parent->is_blank_left_valid()) exit(2);
            permution[blank_idx] = permution[blank_idx - 1];
            blank_idx = blank_idx - 1;
            permution[blank_idx] = 0;
            
        } else if(arg_move == BLANK_RIGHT) {
            if(!arg_parent->is_blank_right_valid()) exit(2);
            permution[blank_idx] = permution[blank_idx + 1];
            blank_idx = blank_idx + 1;
            permution[blank_idx] = 0;
            
        } else {
            exit(2);
        }

        this->heu_ham = calc_ham();
        this->heu_man = calc_man();
        this->known_cost = arg_parent->known_cost+1;
        this->move = arg_move;
    }

    bool is_blank_up_valid() {
        if(blank_idx < 0 || blank_idx >= n*n) return false;
        return !(blank_idx<n);
    }

    bool is_blank_down_valid() {
        if(blank_idx < 0 || blank_idx >= n*n) return false;
        return !(blank_idx>=n*n-n);
    }

    bool is_blank_left_valid() {
        if(blank_idx < 0 || blank_idx >= n*n) return false;
        return (blank_idx % n);
    }

    bool is_blank_right_valid() {
        if(blank_idx < 0 || blank_idx >= n*n) return false;
        return ((blank_idx+1) % n);
    }

    bool is_blank_up_explorable() {
        if(this->parent != NULL && this->move == BLANK_DOWN) return false;
        return is_blank_up_valid();

    }

    bool is_blank_down_explorable() {
        if(this->parent != NULL && this->move == BLANK_UP) return false;
        return is_blank_down_valid();
    }

    bool is_blank_left_explorable() {
        if(this->parent != NULL && this->move == BLANK_RIGHT) return false;
        return is_blank_left_valid();
    }

    bool is_blank_right_explorable() {
        if(this->parent != NULL && this->move == BLANK_LEFT) return false;
        return is_blank_right_valid();
    }


    int get_heu_ham(bool force_calc = false) {
        if(force_calc) {
            this->heu_ham = calc_ham();
        }
        return heu_ham;
    }

    int get_heu_man(bool force_calc = false) {
        if(force_calc) {
            this->heu_man = calc_man();
        }
        return heu_man;
    }


    int get_inv() {
        int temp = 0;
        for(int i = 0; i < n*n - 1; i++) {
            if(i == blank_idx) continue;
            for(int j = i + 1; j < n*n; j++) {
                if(j == blank_idx) continue;
                if(permution[i] > permution[j]) temp++;
            }
        }
        return temp;
    }

    void print_state() {
        for(int i = 0; i<n; i++){
            for(int j=0; j<n;j++) {
                if(blank_idx == i*n +j) {cout<<"*"<<" "; continue;}
                cout<<permution[i*n+j]<<" ";
            }
            cout<<"\n";
        }
    }


    bool is_solvable() {
        int is_odd_inv = get_inv() & 1;
        
        if(n&1) {
            return !is_odd_inv;
        }

        int blank_idx_row = blank_idx / n;
        int is_odd_row = (n - blank_idx_row)&1;
        if(is_odd_inv ^ is_odd_row) return true;
        return false;
    }
    
    int get_known_cost() {
        return known_cost;
    }

    bool equals(State *arg) {
        if(this->blank_idx != arg->blank_idx) return false;
        for(int i = 0; i < n*n; i++) {
            if(i==this->blank_idx) continue;
            if(this->permution[i] != arg->permution[i]) return false;
        }
        return true;
    }


    vector<int> get_permutation() {
        return permution;
    }


    bool is_solved() {
        return !get_heu_ham();
    }

    State * get_parent() {
        return this->parent;
    }

    int get_blank_idx() {
        return blank_idx;
    }

    Move get_blank_move() {
        return move;
    }

};