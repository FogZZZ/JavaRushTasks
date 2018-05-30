package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    private Entry<String> root;
    private Entry<String> parentForNewElementOnNewLine;
    private int size;
    private int lines;

    public CustomTree() {
        this.root = new Entry<>("0");
        root.lineNumber = 0;
        lines = 0;
        size = 0;
    }

    @Override
    public boolean add(String s) {
        Entry<String> parentToAddElement = findParentForNewElement(root);

        if (parentToAddElement == null) {
            return addElement(parentForNewElementOnNewLine, true, s);
        }

        if (parentToAddElement.availableToAddLeftChildren) {
            return addElement(parentToAddElement, true, s);
        }

        if (parentToAddElement.availableToAddRightChildren) {
            return addElement(parentToAddElement, false, s);
        }

        return false;
    }

    boolean addElement(Entry<String> parent, boolean isLeft, String s) {
        Entry<String> child = new Entry<>(s);
        if (isLeft) {
            parent.leftChild = child;
        } else {
            parent.rightChild = child;
        }
        child.parent = parent;
        child.lineNumber = parent.lineNumber + 1;
        lines = child.lineNumber;
        size++;
        parentForNewElementOnNewLine = null;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    String getParent(String s) {
        Entry<String> element = findElement(root, s);
        if (element != null) {
            return element.parent.elementName;
        } else {
            return null;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof String)) {
            throw new UnsupportedOperationException();
        }
        String s = (String)o;

        Entry<String> elementToRemove = findElement(root, s);

        if (elementToRemove == null) {
            return false;
        }

        if (elementToRemove.parent.leftChild == elementToRemove) {
            elementToRemove.parent.leftChild = null;
        } else {
            elementToRemove.parent.rightChild = null;
        }

        lines = getMaxLine(root, 0);
        size -= getSizeOfSubtree(elementToRemove);
        return true;
    }

    Entry<String> findElement(Entry<String> currentElement, String s) {
        if (currentElement == null) {
            return null;
        }

        if (currentElement.elementName.equals(s)) {
            return currentElement;
        }
        Entry<String> leftFind = findElement(currentElement.leftChild, s);
        if (leftFind != null) {
            return leftFind;
        }
        Entry<String> rightFind = findElement(currentElement.rightChild, s);
        if (rightFind != null) {
            return  rightFind;
        }
        return null;
    }

    Entry<String> findParentForNewElement(Entry<String> currentElement) {
        currentElement.checkChildren();

        if (lines == 0 || currentElement.lineNumber == lines-1) {
            if (currentElement.isAvailableToAddChildren()) {
                return currentElement;
            } else {
                if (parentForNewElementOnNewLine == null) {
                    parentForNewElementOnNewLine = currentElement.leftChild == null ? currentElement.rightChild : currentElement.leftChild;
                }
                return null;
            }
        }

        if (currentElement.leftChild != null) {
            Entry<String> leftFind = findParentForNewElement(currentElement.leftChild);
            if (leftFind != null) {
                return leftFind;
            }
        }

        if (currentElement.rightChild != null) {
            Entry<String> rightFind = findParentForNewElement(currentElement.rightChild);
            if (rightFind != null) {
                return rightFind;
            }
        }
        return null;
    }

    int getMaxLine(Entry<String> currentEntry, int currentMaxLine) {
        currentEntry.checkChildren();
        if (currentMaxLine < currentEntry.lineNumber) {
            currentMaxLine = currentEntry.lineNumber;
        }
        if (currentEntry.leftChild != null) {
            currentMaxLine = getMaxLine(currentEntry.leftChild, currentMaxLine);
        }
        if (currentEntry.rightChild != null) {
            currentMaxLine = getMaxLine(currentEntry.rightChild, currentMaxLine);
        }
        return currentMaxLine;
    }

    int getSizeOfSubtree(Entry<String> currentEntry) {
        currentEntry.checkChildren();
        int currentSize = 1;
        if (currentEntry.leftChild != null) {
            currentSize += getSizeOfSubtree(currentEntry.leftChild);
        }
        if (currentEntry.rightChild != null) {
            currentSize += getSizeOfSubtree(currentEntry.rightChild);
        }
        return currentSize;
    }


    static class Entry<T> implements Serializable {
        String elementName;
        int lineNumber;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        void checkChildren() {
            if (leftChild != null) {
                availableToAddLeftChildren = false;
            }
            if (rightChild != null) {
                availableToAddRightChildren = false;
            }
        }

        boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }
    }







    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }
}
