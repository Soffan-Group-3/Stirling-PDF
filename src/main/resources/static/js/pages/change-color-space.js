var canvas = document.getElementById('contrast-pdf-canvas');
var context = canvas.getContext('2d');
var originalImageData = null;
var allPages = [];
var pdfDoc = null;
var pdf = null; // This is the current PDF document

async function renderPDFAndSaveOriginalImageData(file) {
  var fileReader = new FileReader();
  fileReader.onload = async function () {
    var data = new Uint8Array(this.result);
    pdfjsLib.GlobalWorkerOptions.workerSrc = './pdfjs-legacy/pdf.worker.mjs';
    pdf = await pdfjsLib.getDocument({data: data}).promise;

    // Get the number of pages in the PDF
    var numPages = pdf.numPages;
    allPages = Array.from({length: numPages}, (_, i) => i + 1);

    // Create a new PDF document
    pdfDoc = await PDFLib.PDFDocument.create();
    // Render the first page in the viewer
    await renderPageAndAdjustImageProperties(1);
  };
  fileReader.readAsArrayBuffer(file);
}

// This function is now async and returns a promise
function renderPageAndAdjustImageProperties(pageNum) {
  return new Promise(async function (resolve, reject) {
    var page = await pdf.getPage(pageNum);
    var scale = 1.5;
    var viewport = page.getViewport({scale: scale});

    canvas.height = viewport.height;
    canvas.width = viewport.width;

    var renderContext = {
      canvasContext: context,
      viewport: viewport,
    };

    var renderTask = page.render(renderContext);
    renderTask.promise.then(function () {
      originalImageData = context.getImageData(0, 0, canvas.width, canvas.height);
      adjustImageProperties();
      resolve();
    });
    canvas.classList.add('fixed-shadow-canvas');
  });
}

function adjustImageProperties() {
  var target_space = parseFloat(document.getElementById('colorChange').value);
  console.log(target_space);


  if (originalImageData) {
    var newImageData = context.createImageData(originalImageData.width, originalImageData.height);
    newImageData.data.set(originalImageData.data);

    for (var i = 0; i < newImageData.data.length; i += 4) {
      var r = newImageData.data[i];
      var g = newImageData.data[i + 1];
      var b = newImageData.data[i + 2];

      newImageData.data[i] = r;
      newImageData.data[i + 1] = g;
      newImageData.data[i + 2] = b;
    }
    context.putImageData(newImageData, 0, 0);
  }
}

function rgbToCmyk(r, g, b) {
  
  var c = 1 - r / 255;
  var m = 1 - g / 255;
  var y = 1 - b / 255;

  
  var k = Math.min(c, m, y);

  
  if (k === 1) {
    return [0, 0, 0, 1];
  }

  // Convert CMY to CMYK
  c = (c - k) / (1 - k);
  m = (m - k) / (1 - k);
  y = (y - k) / (1 - k);

  return [c, m, y, k];
}


let inputFileName = '';
async function downloadPDF() {
  for (var i = 0; i < allPages.length; i++) {
    await renderPageAndAdjustImageProperties(allPages[i]);
    const pngImageBytes = canvas.toDataURL('image/png');
    const pngImage = await pdfDoc.embedPng(pngImageBytes);
    const pngDims = pngImage.scale(1);

    // Create a blank page matching the dimensions of the image
    const page = pdfDoc.addPage([pngDims.width, pngDims.height]);

    // Draw the PNG image
    page.drawImage(pngImage, {
      x: 0,
      y: 0,
      width: pngDims.width,
      height: pngDims.height,
    });
  }

  // Serialize the PDFDocument to bytes (a Uint8Array)
  const pdfBytes = await pdfDoc.save();

  // Create a Blob
  const blob = new Blob([pdfBytes.buffer], {type: 'application/pdf'});

  // Create download link
  const downloadLink = document.createElement('a');
  downloadLink.href = URL.createObjectURL(blob);
  let newFileName = inputFileName ? inputFileName.replace('.pdf', '') : 'download';
  newFileName += '_adjusted_color.pdf';

  downloadLink.download = newFileName;
  downloadLink.click();

  // After download, reset the viewer and clear stored data
  allPages = []; // Clear the pages
  originalImageData = null; // Clear the image data

  // Go back to page 1 and render it in the viewer
  if (pdf !== null) {
    renderPageAndAdjustImageProperties(1);
  }
}

// Event listeners
document.getElementById('fileInput-input').addEventListener('change', function (e) {
  const fileInput = e.target;
  fileInput.addEventListener('file-input-change', async (e) => {
    const {allFiles} = e.detail;
    if (allFiles && allFiles.length > 0) {
      const file = allFiles[0];
      inputFileName = file.name;
      renderPDFAndSaveOriginalImageData(file);
    }
  });
});

document.getElementById('colorChange').addEventListener('input', function () {
  document.getElementById('colorChange').textContent = this.value;
  adjustImageProperties();
});

document.getElementById('download-button').addEventListener('click', function () {
  downloadPDF();
});
