#include <iostream>
#include <math.h>
#include <array>
#include <vector>
using namespace std;


#define NLOG2N(x) ((x) * log2(x))


double nlog2n(double n) {
    return n * log2(n);
}

// entropy of a source, or average self-information per sample
// assuming all events independent
double entropy(vector<double> &probabilities) {
    double sum = 0;
    for (auto p : probabilities) {
        sum += NLOG2N(p);
    }
    return -sum;
}

int main() {

    cout << nlog2n(4) << endl;
    cout << nlog2n(2) << endl;
    cout << nlog2n(1) << endl;
    cout << nlog2n(0.5) << endl;
    cout << nlog2n(0.25) << endl;
    cout << nlog2n(0.125) << endl;
    cout << NLOG2N(0.125) << endl;
    cout << log2(.1) << endl;

    vector<double> myArray {(2.0/16), (2.0/16), (2.0/16), (2.0/16), (2.0/16), (2.0/16),  (1.0/16), (1.0/16),(1.0/16),(1.0/16) };

    cout << entropy(myArray) << endl;




    return 0;
}
