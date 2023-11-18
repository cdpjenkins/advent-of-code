#include "day04.hpp"

#include <iostream>
#include <sstream>

#include "util.hpp"

class Assignment {
    int start;
    int end;

public:
    explicit Assignment() : start{-1}, end{-1} {}
    Assignment(int start, int end) : start(start), end(end) {}

    friend std::istream& operator>>(std::istream& s, Assignment& a) {
        char delimiter;

        s >> a.start >> delimiter >> a.end;

        return s;
    }

    bool fully_contains(const Assignment& that) const {
        return this->start <= that.start && this->end >= that.end;
    }
};

int day04_part1(const std::vector<std::string> &input) {

    std::vector<std::pair<Assignment, Assignment>> v;

    for (const auto &item: input) {
        char comma;

        std::stringstream stream(item);
        Assignment ass1;
        Assignment ass2;

        stream >> ass1 >> comma >> ass2;

        v.emplace_back(ass1, ass2);
    }

    int count = 0;
    for (const auto &item: v) {
        if (item.first.fully_contains(item.second) || item.second.fully_contains(item.first)) {
            count++;
        }
    }

    return count;
}

int day04_part2(const std::vector<std::string> &input) {
    return -1;
}
