const URL = 'http://localhost:8080';
let entries = [];
let updating = false;

const dateAndTimeToDate = (dateString, timeString) => {
    return moment.utc(`${dateString}T${timeString}`).toISOString();
};

const createEntry = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const entry = {};
    entry['checkIn'] = dateAndTimeToDate(formData.get('checkInDate'), formData.get('checkInTime'));
    entry['checkOut'] = dateAndTimeToDate(formData.get('checkOutDate'), formData.get('checkOutTime'));

    fetch(`${URL}/entry`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(entry)
    }).then((result) => {
        result.json().then((entry) => {
            entries.push(entry);
            renderEntries();
        });
    });
};

function updateEntry(e){
    e.preventDefault();
    const formData = new FormData(e.target);
    const entry = {};
    entry["id"] = formData.get('id');
    entry['checkIn'] = dateAndTimeToDate(formData.get('checkInDate'), formData.get('checkInTime'));
    entry['checkOut'] = dateAndTimeToDate(formData.get('checkOutDate'), formData.get('checkOutTime'));

    fetch(`${URL}/entry`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(entry)
    }).then((result) => {
        result.json().then((newEntry) => {
            const oldEntry = entries.find(entry => entry.id === newEntry.id);
            Object.assign(oldEntry, newEntry);
            renderEntries();
        });
    });
}

function deleteEntry(event){
   const id = event.target.dataset.entryId;
   fetch(`${URL}/entry/${id}`, {
       method: "DELETE"
   }).then(() => {
       const entry = entries.find(entry => entry.id == id);
       entries.splice(entries.indexOf(entry), 1);
       renderEntries();
       clearUpdate();
   });
}

const indexEntries = () => {
    fetch(`${URL}/entries`, {
        method: 'GET'
    }).then((result) => {
        result.json().then((result) => {
            entries = result;
            renderEntries();
        });
    });
    renderEntries();
};

const createCellWithText = (text) => {
    const cell = document.createElement('td');
    cell.innerText = text;
    return cell;
};

function createCellWithElement(element){
    const cell = document.createElement('td');
    cell.appendChild(element);
    return cell;
}

const renderEntries = () => {
    const display = document.querySelector('#entryDisplay');
    display.innerHTML = '';
    entries.forEach((entry) => {
        const row = document.createElement('tr');
        row.appendChild(createCellWithText(entry.id));
        row.appendChild(createCellWithText(new Date(entry.checkIn).toLocaleString()));
        row.appendChild(createCellWithText(new Date(entry.checkOut).toLocaleString()));
        row.appendChild(createCellWithElement(createDeleteButton(entry.id)));
        row.appendChild(createCellWithElement(createUpdateButton(entry.id)));
        display.appendChild(row);
    });
};

function renderUpdateEntry(event){
    const id = event.target.dataset.entryId;
    const entry = entries.find(entry => entry.id == id);
    const form = document.getElementById("createEntryForm");
    form.elements["id"].value = entry.id;
    form.elements["checkInDate"].value = moment(entry.checkIn).format("YYYY-MM-DD");
    form.elements["checkInTime"].value = moment(entry.checkIn).format("HH:mm");
    form.elements["checkOutDate"].value = moment(entry.checkOut).format("YYYY-MM-DD");
    form.elements["checkOutTime"].value = moment(entry.checkOut).format("HH:mm");
    document.getElementById("saveButton").value = "Update";
    document.getElementById("clearButton").className = "";
    updating = true;
}

function clearUpdate(){
    const form = document.getElementById("createEntryForm");
    form.elements["id"].value = 0;
    form.elements["checkInDate"].value = "";
    form.elements["checkInTime"].value = "";
    form.elements["checkOutDate"].value = "";
    form.elements["checkOutTime"].value = "";
    document.getElementById("saveButton").value = "Save";
    document.getElementById("clearButton").className = "hidden";
    updating = false;
}

function createDeleteButton(entryId){
    const button = document.createElement('button');
    button.dataset.entryId = entryId;
    button.onclick = function (event){
        deleteEntry(event);
    }
    button.innerHTML = "Delete";
    return button;
}

function createUpdateButton(entryId){
    const button = document.createElement('button');
    button.dataset.entryId = entryId;
    button.onclick = function (event){
        renderUpdateEntry(event);
    }
    button.innerHTML = "Update";
    return button;
}

document.addEventListener('DOMContentLoaded', function(){
    const createEntryForm = document.querySelector('#createEntryForm');
    createEntryForm.addEventListener('submit', function (e){
        if(updating){
            updateEntry(e);
        }else {
            createEntry(e);
        }
    });
    indexEntries();
});
