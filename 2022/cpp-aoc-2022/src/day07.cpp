#include "day07.hpp"

#include <iostream>
#include <map>
#include <utility>

namespace Day07 {

    struct Directory {
        std::string name;
        std::map<std::string, std::unique_ptr<Directory>> sub_dirs;
        int size = 0;

        Directory(std::string name) : name(std::move(name)) {

        }

        Day07::Directory* make_child(const std::string& dir_name) {
            if (sub_dirs[dir_name] == nullptr) {
                sub_dirs[dir_name] = std::make_unique<Directory>(dir_name);
            }

            return sub_dirs[dir_name].get();
        }

        void add_file(int file_size) {
            size += file_size;
        }

        int compute_size() {
            int computed_size = this->size;

            for (auto& [_, sub_dir] : sub_dirs) {
                computed_size += sub_dir->compute_size();
            }

            return computed_size;
        }
    };

    struct Tree {
        std::unique_ptr<Directory> root;
        std::vector<Directory*> directory_stack;

        Tree() : root(std::make_unique<Directory>("/"))
        {
            directory_stack.push_back(root.get());
        }

        void cd(std::string& to_dir) {
            if (to_dir == "/") {
                directory_stack.clear();
                directory_stack.push_back(root.get());
            } else if (to_dir == "..") {
                directory_stack.pop_back();
            } else {
                Directory* current_directory = directory_stack.back();
                Directory* new_directory = current_directory->make_child(to_dir);
                directory_stack.push_back(new_directory);;
            }
        }

        void add_file(int file_size) {
            Directory* current_directory = directory_stack.back();
            current_directory->add_file(file_size);
        }
    };

    struct Command {
        enum class Type { NONE, CD, LS };

        friend std::istream& operator>>(std::istream& stream, Command& command);

        Type type{Type::NONE};
        std::string target_dir;
        std::vector<int> file_sizes;

        void execute(Tree& tree) {
            switch (type) {
                case Type::CD:
                    tree.cd(target_dir);
                    break;
                case Type::LS:
                    for (int file_size : file_sizes) {
                        tree.add_file(file_size);
                    }
                    break;
                default:
                    throw std::runtime_error("arghghghgh " + std::to_string(static_cast<int>(type)));
            }
        }
    };

    void skip_whitespace(std::istream& stream) {
        // TODO - consider refactoring me. If we forget to consume a single tokem on one line then we get out of sync
        // with lines of input and everything goes a bit screwy
        //
        // Ideally we'd read the input a line a
        while (std::isspace(stream.peek())) stream.get();
    }

    std::istream& operator>>(std::istream& stream, Command& command) {
        char _;
        std::string command_string;

        stream >> _ >> command_string;

        std::cout << _ << " " << command_string << std::endl;;

        if (command_string == "cd") {
            std::string target_dir;

            stream >> target_dir;

            skip_whitespace(stream);

            command.type = Command::Type::CD;
            command.target_dir = target_dir;
        } else if (command_string == "ls") {
            command.type = Command::Type::LS;
            skip_whitespace(stream);

            command.type = Command::Type::LS;

            while (stream.peek() != '$' && !stream.eof()) {
                // std::string ston;
                // std::getline(stream, ston);
                // std::cout << ston << std::endl;

                if (std::isdigit(stream.peek())) {
                    int size;
                    std::string filename;       // which we are going to ignore for now
                    stream >> size >> filename;;

                    command.file_sizes.push_back(size);
                } else {
                    // meh it's a directory entry but we'll find out about that when we cd into it
                    std::string _;
                    std::string dir_name;

                    stream >> _ >> dir_name;

                    std::cout << "waaa: " << _ << " " << dir_name << std::endl;
                }
                skip_whitespace(stream);
            }
        }

        return stream;
    }

    int part1(std::istream& input) {

        std::vector<Command> commands;
        while (input.peek() == '$') {
            Command command;
            input >> command;

            commands.push_back(command);
        }

        Tree tree;

        for (Command& command : commands) {
            command.execute(tree);
        }

        std::cout << "total_size: " << tree.root->compute_size() << std::endl;

        return 123;
    }

    int part2(std::string input) {
        return 1234;
    }
}
