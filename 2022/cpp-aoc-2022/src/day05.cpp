#include "day04.hpp"

#include <iostream>

#include <ranges>

#include "util.hpp"

struct Command {
    int num;
    int from;
    int to;

    void execute_on_cratemover_9000(std::vector<std::vector<char>>& stacks) const {
        for (int i = 0; i < num; i++) {
            char c = stacks[from].back();
            stacks[from].pop_back();
            stacks[to].push_back(c);
        }
    }

    void execute_on_cratemover_9001(std::vector<std::vector<char>>& stacks) const {
        std::vector<char> temp;

        for (int i = 0; i < num; i++) {
            char c = stacks[from].back();
            stacks[from].pop_back();
            temp.push_back(c);
        }

        for (int i = 0; i < num; i++) {
            char c = temp.back();
            temp.pop_back();

            stacks[to].push_back(c);
        }
    }

    friend std::istream &operator>>(std::istream &stream, Command &command) {
        std::string _;

        stream >> _ >> command.num >> _ >> command.from >> _ >> command.to;

        command.from--;
        command.to--;

        return stream;
    }
};

static std::vector<std::vector<char>> parse_stacks(std::istream &input) {
    std::string line;
    std::vector<std::vector<char>> stacks;
    while (std::getline(input, line)) {
        if (std::any_of(line.begin(), line.end(), [](char c){return std::isdigit(c);})) {
            break;
        }

        for (int i = 1; i < line.length(); i += 4) {
            int stack_index = (i - 1) / 4;
            if (stack_index >= stacks.size()) {
                stacks.resize(stack_index + 1);
            }

            char c = line[i];
            if (std::isupper(c)) {
                stacks[stack_index].push_back(line[i]);
            }
        }
    }

    for (auto &stack: stacks) {
        std::reverse(stack.begin(), stack.end());

//        for (const auto &item: stack) {
//            std::cout << item;
//        }
//        std::cout << std::endl;
    }

    // consume blank line
    std::getline(input, line);

    return stacks;
}

static std::vector<Command> parse_commands(std::istream &input) {
    std::vector<Command> commands;
    std::ranges::copy(std::views::istream<Command>(input), std::back_inserter(commands));

    return commands;
}

std::string day05_part1(std::istream &input) {
    std::vector<std::vector<char>> stacks{parse_stacks(input)};
    std::vector<Command> commands{parse_commands(input)};

    for (const auto &command: commands) {
        command.execute_on_cratemover_9000(stacks);
    }

    std::string result;
    for (const auto &stack: stacks) {
        result += stack.back();
    }

    return result;
}

std::string day05_part2(std::istream &input) {
    std::vector<std::vector<char>> stacks{parse_stacks(input)};
    std::vector<Command> commands{parse_commands(input)};

    for (const auto &command: commands) {
        command.execute_on_cratemover_9001(stacks);
    }

    std::string result;
    for (const auto &stack: stacks) {
        result += stack.back();
    }

    return result;
}
