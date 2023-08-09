#include "state.cpp"

int ham_expand;
int ham_explored;

int man_expand;
int man_explored;

class MinCompHamming {
    public:
    bool operator()(State* arg1, State* arg2) {
        // if((arg1->get_known_cost()+arg1->get_heu_ham()) == (arg2->get_known_cost()+arg2->get_heu_ham())) {
        //     if (arg1->get_heu_ham() == arg2->get_heu_ham()) return arg1->get_blank_idx() > arg2->get_blank_idx();
        //     return arg1->get_heu_ham() > arg2->get_heu_ham();
        // }
        return (arg1->get_known_cost()+arg1->get_heu_ham()) > (arg2->get_known_cost()+arg2->get_heu_ham());
    }
};

class MinCompManhatton {
    public:
    bool operator()(State* arg1, State* arg2) {
        // if((arg1->get_known_cost()+arg1->get_heu_man()) == (arg2->get_known_cost()+arg2->get_heu_man())) {
        //     if (arg1->get_heu_man() == arg2->get_heu_man()) return arg1->get_blank_idx() > arg2->get_blank_idx();
        //     return arg1->get_heu_man() > arg2->get_heu_man();
        // }
        return (arg1->get_known_cost()+arg1->get_heu_man()) > (arg2->get_known_cost()+arg2->get_heu_man());
    }
};

vector<State *> a_star_man(State * start) {
    if(!start->is_solvable()) return vector<State *>();

    map<vector<int>, int> openlist;
    map<vector<int>, int> closedlist;

    priority_queue<State*, vector<State*>, MinCompManhatton> pq;
    man_expand = 0;
    man_explored = 0;

    pq.push(start);
    openlist[start->get_permutation()] = start->get_heu_man();
    vector<State*> soln;
    while(true) {
        State* current = pq.top();
        pq.pop();
        if(!openlist.count(current->get_permutation())) continue;
        openlist.erase(openlist.find(current->get_permutation()));
        man_explored++;

        closedlist[current->get_permutation()] = current->get_known_cost() + current->get_heu_man();

        if(current->is_solved()) {
            State * temp = current;
            while(temp != NULL) {
                soln.push_back(temp);
                temp = temp->get_parent();
            }

            reverse(soln.begin(), soln.end());
            return soln;
        }

        vector<State *> children;

        if(current->is_blank_down_explorable()) children.push_back(new State(current, BLANK_DOWN));
        if(current->is_blank_right_explorable()) children.push_back(new State(current, BLANK_RIGHT));
        if(current->is_blank_up_explorable()) children.push_back(new State(current, BLANK_UP));
        if(current->is_blank_left_explorable()) children.push_back(new State(current, BLANK_LEFT));
        
        
        

        for(auto child : children) {
            
            
            if(child->is_solved()) {
                State * temp = child;
                while(temp != NULL) {
                    soln.push_back(temp);
                    temp = temp->get_parent();
                }
                man_explored++;
        
                reverse(soln.begin(), soln.end());
                return soln;
            }


            if(closedlist.count(child->get_permutation())
                || (openlist.count(child->get_permutation())
                    && (child->get_known_cost() + child->get_heu_man() >= openlist[child->get_permutation()]))) {
                delete child;
                continue;
            }

            pq.push(child);
            openlist[child->get_permutation()] = child->get_known_cost() + child->get_heu_man();
            man_expand++;

        }

        children.clear();

    }
    
    
}



vector<State *> a_star_ham(State * start) {
    if(!start->is_solvable()) return vector<State *>();

    map<vector<int>, int> openlist;
    map<vector<int>, int> closedlist;

    priority_queue<State*, vector<State*>, MinCompHamming> pq;
    ham_expand = 0;
    ham_explored = 0;

    pq.push(start);
    openlist[start->get_permutation()] = start->get_heu_ham();
    vector<State*> soln;
    while(true) {
        State* current = pq.top();
        pq.pop();
        if(!openlist.count(current->get_permutation())) continue;
        openlist.erase(openlist.find(current->get_permutation()));
        ham_explored++;

        closedlist[current->get_permutation()] = current->get_known_cost() + current->get_heu_ham();

        if(current->is_solved()) {
            State * temp = current;
            while(temp != NULL) {
                soln.push_back(temp);
                temp = temp->get_parent();
            }

            reverse(soln.begin(), soln.end());
            return soln;
        }

        vector<State *> children;

        if(current->is_blank_down_explorable()) children.push_back(new State(current, BLANK_DOWN));
        if(current->is_blank_right_explorable()) children.push_back(new State(current, BLANK_RIGHT));
        if(current->is_blank_left_explorable()) children.push_back(new State(current, BLANK_LEFT));
        if(current->is_blank_up_explorable()) children.push_back(new State(current, BLANK_UP));
        
        
        

        for(auto child : children) {
            
            
            if(child->is_solved()) {
                State * temp = child;
                while(temp != NULL) {
                    soln.push_back(temp);
                    temp = temp->get_parent();
                }
                ham_explored++;
        
                reverse(soln.begin(), soln.end());
                return soln;
            }


            if(closedlist.count(child->get_permutation())
                || (openlist.count(child->get_permutation())
                    && (child->get_known_cost() + child->get_heu_ham() >= openlist[child->get_permutation()]))) {
                delete child;
                continue;
            }

            pq.push(child);
            openlist[child->get_permutation()] = child->get_known_cost() + child->get_heu_ham();
            ham_expand++;

        }

        children.clear();

    }
    
    
}





int main() {
    int n;
    cin>>n; // n in code is k in the spec. n-1 in code is n in code
    int arr[n*n];
    for(int i = 0; i < n*n; i++) {
        cin>>arr[i];
    }


    /*
     * MAN
     * #############################################
     * 
     */


    State* start = new State(n,arr);

    vector<State *> soln = a_star_man(start);

    if(soln.empty()) {
        cout<<"NO SOLUTION"<<endl;
        return 0;
    }

    cout<<"MANHATTON EXPANDED :"<<man_expand<<endl;
    cout<<"MANHATTON EXPLORED :"<<man_explored<<endl<<endl;

    cout<<"MANHATTON STEPS: "<<soln.size() - 1<<endl;

    
    for (auto i : soln) {
        if(i->get_parent() != NULL) {
            if(i->get_blank_move() == BLANK_DOWN) cout<<"tile up"<<endl;
            else if(i->get_blank_move() == BLANK_RIGHT) cout<<"tile left"<<endl;
            else if(i->get_blank_move() == BLANK_UP) cout<<"tile down"<<endl;
            else if(i->get_blank_move() == BLANK_LEFT) cout<<"tile right"<<endl;
        }


        i->print_state();
        cout<<endl;
    }



    /*
     * HAM
     * #############################################
     * 
     */

    State* start2 = new State(n,arr);

    vector<State *> soln2 = a_star_ham(start);

    if(soln2.empty()) {
        cout<<"NO SOLUTION"<<endl;
        return 0;
    }

    cout<<"HAMMING EXPANDED :"<<ham_expand<<endl;
    cout<<"HAMMING EXPLORED :"<<ham_explored<<endl<<endl;

    cout<<"HAMMING STEPS: "<<soln2.size() - 1<<endl;

    
    for (auto i : soln2) {
        if(i->get_parent() != NULL) {
            if(i->get_blank_move() == BLANK_DOWN) cout<<"tile up"<<endl;
            else if(i->get_blank_move() == BLANK_RIGHT) cout<<"tile left"<<endl;
            else if(i->get_blank_move() == BLANK_UP) cout<<"tile down"<<endl;
            else if(i->get_blank_move() == BLANK_LEFT) cout<<"tile right"<<endl;
        }

        i->print_state();
        cout<<endl;
    }

    return 0;
}