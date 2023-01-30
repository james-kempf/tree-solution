function addEdge() {
    let table = document.getElementById("edge-table-body");

    let row = table.insertRow(-1);

    let parentCell = row.insertCell(0);
    let childCell = row.insertCell(1);
    let removeCell = row.insertCell(2);

    parentCell.appendChild(createEdgeInput());

    childCell.appendChild(createEdgeInput());

    let removeButton = document.createElement("button");
    removeButton.onclick = () => removeEdge(row);
    removeButton.innerText = "X";
    removeButton.className = "btn btn-danger";
    removeCell.appendChild(removeButton);
}

function clearInput(elementId) {
    document.getElementById(elementId).value = "";
    updateTree();
}

function removeEdge(row) {
    row.remove();
    updateTree();
}

function createEdgeInput() {
    let input = document.createElement("input");
    input.className = "edge-input form-control";
    input.oninput = () => updateTree();
    return input;
}

function updateTree() {
    try {
        let table = document.getElementById("edge-table-body");
        let errorMessage = document.getElementById("error-message");
        errorMessage.innerText = "";
        errorMessage.hidden = true;
        let edgeList = [];
        for (let i = 0, row; row = table.rows[i]; i++) {
            let parent = row.children[0].firstChild.value;
            let child = row.children[1].firstChild.value;
            if (parent !== "" && child !== "") {
                edgeList.push([
                    row.children[0].firstChild.value,
                    row.children[1].firstChild.value
                ]);
            }
        }

        let root = document.getElementById("root");

        // clear previous tree
        let previousChildren = root.children[1];
        if (previousChildren !== undefined) {
            previousChildren.remove();
        }

        let createdNodes = ["1"];
        createEdges(root, edgeList, createdNodes);

        document.getElementById("first-entry-remove").hidden =
            document.getElementById("first-child").value === "";
    } catch (error) {
        console.error(error);
    }
}

function createEdges(node, edgeList, createdNodes) {
    let nodeValue = node.children[0].innerText;
    // group edges by whether current node is the parent or not
    let edgesAsParent = [];
    let edgesNotAsParent = [];
    for (let i = 0, edge; edge = edgeList[i]; i++) {
        if (edge.includes(nodeValue)) {
            edgesAsParent.push(edge);
        } else {
            edgesNotAsParent.push(edge);
        }
    }
    if (edgesAsParent.length !== 0) {
        // create all child nodes
        for (let i = 0, edge; edge = edgesAsParent[i]; i++) {
            createChild(node, edge, edgesNotAsParent, createdNodes);
        }
    }
    return node;
}

function createChild(node, edge, edgeList, createdNodes) {
    // get child value from either first or second entry
    let a = edge[0];
    let b = edge[1];
    let v = node.children[0].innerText;
    let childValue = edge[0] === node.children[0].innerText ? edge[1] : edge[0];

    // validate child does not already have a parent
    if (createdNodes.includes(childValue)) {
        showError("Node " + childValue + " has too many parents");
        return true;
    }
    createdNodes.push(childValue);

    // create child element
    let childNode = document.createElement("li");
    let value = document.createElement("span");
    value.innerText = childValue;
    value.className = "tf-nc";
    childNode.appendChild(value);

    // append child to parent node
    let children = node.children[1];
    if (children === undefined) {
        children = document.createElement("ul");
        node.appendChild(children);
    }
    children.appendChild(childNode);

    // create children of child node
    createEdges(childNode, edgeList, createdNodes);
}

function showError(message) {
    let errorMessage = document.getElementById("error-message");
    errorMessage.innerText = message;
    errorMessage.hidden = false;
}
